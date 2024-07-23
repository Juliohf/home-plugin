package com.juliohf.homePlugin;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.*;
import java.util.HashMap;
import java.util.UUID;

public final class HomePlugin extends JavaPlugin {

    private Connection connection;
    private HashMap<UUID, Long> cooldowns = new HashMap<>();
    private long cooldownTime;
    private Particle particleEffect;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        cooldownTime = getConfig().getLong("cooldown", 60) * 1000; // Convert to milliseconds
        String particleEffectName = getConfig().getString("particle-effect", "FLAME");

        try {
            particleEffect = Particle.valueOf(particleEffectName.toUpperCase());
            getLogger().info("Usando o efeito: " + particleEffectName);
        } catch (IllegalArgumentException e) {
            getLogger().warning("Efeito de partícula invalida (" + particleEffectName + "), retornando para o efeito FLAME");
            particleEffect = Particle.FLAME;
        }

        String url = "jdbc:mysql://localhost:3306/";
        String user = "root";
        String password = "";

        try {
            connection = DriverManager.getConnection(url, user, password);
            try (Statement statement = connection.createStatement()) {
                statement.executeUpdate("CREATE DATABASE IF NOT EXISTS minecraft");
            }
            connection.close();

            // Reconnect to the newly created database
            url = "jdbc:mysql://localhost:3306/minecraft";
            connection = DriverManager.getConnection(url, user, password);
            getLogger().info("Conectado com sucesso!");
        } catch (Exception e) {
            getLogger().severe("Não pode conectar com o banco de dados!");
            e.printStackTrace();
        }

        if (connection != null) {
            // Create home table if not exists
            try (PreparedStatement statement = connection.prepareStatement(
                    "CREATE TABLE IF NOT EXISTS homes (uuid VARCHAR(36), home_name VARCHAR(255), world VARCHAR(255), x DOUBLE, y DOUBLE, z DOUBLE, PRIMARY KEY(uuid, home_name))")) {
                statement.execute();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            getLogger().severe("Conexão com o banco de dados nula, plugin desabilitado.");
            getServer().getPluginManager().disablePlugin(this);
        }
    }

    @Override
    public void onDisable() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                getLogger().info("Desconectado com o banco de dados");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Apenas jogadores podem usar esse comando");
            return true;
        }

        Player player = (Player) sender;
        UUID playerUUID = player.getUniqueId();

        if (command.getName().equalsIgnoreCase("sethome")) {
            if (args.length < 1) {
                player.sendMessage("Por favor especifique o nome da sua home entre aspas");
                return true;
            }

            String homeName = args[0];
            Location location = player.getLocation();
            try (PreparedStatement statement = connection.prepareStatement(
                    "REPLACE INTO homes (uuid, home_name, world, x, y, z) VALUES (?, ?, ?, ?, ?, ?)")) {
                statement.setString(1, playerUUID.toString());
                statement.setString(2, homeName);
                statement.setString(3, location.getWorld().getName());
                statement.setDouble(4, location.getX());
                statement.setDouble(5, location.getY());
                statement.setDouble(6, location.getZ());
                statement.execute();
                player.sendMessage("Home '" + homeName + "' salva com sucesso!");
            } catch (Exception e) {
                e.printStackTrace();
                player.sendMessage("Um erro ocorreu ao tentar salvar o home '" + homeName + "'");
            }
            return true;
        }

        if (command.getName().equalsIgnoreCase("home")) {
            if (args.length < 1) {
                player.sendMessage("Por favor especifique uma home para se teleportar");
                return true;
            }

            String homeName = args[0];
            long currentTime = System.currentTimeMillis();
            if (cooldowns.containsKey(playerUUID) && (currentTime - cooldowns.get(playerUUID)) < cooldownTime) {
                long remainingTime = (cooldownTime - (currentTime - cooldowns.get(playerUUID))) / 1000;
                player.sendMessage("Você precisa aguardar " + remainingTime + " segundos antes de usar esse comando novamente.");
                return true;
            }

            try (PreparedStatement statement = connection.prepareStatement(
                    "SELECT world, x, y, z FROM homes WHERE uuid = ? AND home_name = ?")) {
                statement.setString(1, playerUUID.toString());
                statement.setString(2, homeName);
                ResultSet results = statement.executeQuery();

                if (results.next()) {
                    String world = results.getString("world");
                    double x = results.getDouble("x");
                    double y = results.getDouble("y");
                    double z = results.getDouble("z");

                    Location homeLocation = new Location(Bukkit.getWorld(world), x, y, z);
                    player.getWorld().spawnParticle(particleEffect, player.getLocation(), 100);
                    player.teleport(homeLocation);
                    player.getWorld().spawnParticle(particleEffect, homeLocation, 100);
                    player.sendMessage("Teleported to home '" + homeName + "'.");

                    cooldowns.put(playerUUID, currentTime);
                } else {
                    player.sendMessage("Home '" + homeName + "' não existe use o comando /sethome para salvar uma nova home.");
                }
            } catch (Exception e) {
                e.printStackTrace();
                player.sendMessage("um erro ocorreu ao teleportar para sua home.");
            }
            return true;
        }

        if (command.getName().equalsIgnoreCase("delhome")) {
            if (args.length < 1) {
                player.sendMessage("Por favor especifique uma home para deletar");
                return true;
            }

            String homeName = args[0];
            try (PreparedStatement statement = connection.prepareStatement(
                    "DELETE FROM homes WHERE uuid = ? AND home_name = ?")) {
                statement.setString(1, playerUUID.toString());
                statement.setString(2, homeName);
                int rowsAffected = statement.executeUpdate();
                if (rowsAffected > 0) {
                    player.sendMessage("Home '" + homeName + "' deletada com sucesso!");
                } else {
                    player.sendMessage("Home '" + homeName + "' não existe.");
                }
            } catch (Exception e) {
                e.printStackTrace();
                player.sendMessage("Um erro ocorreu ao deletar sua home.");
            }
            return true;
        }

        if (command.getName().equalsIgnoreCase("homes")) {
            try (PreparedStatement statement = connection.prepareStatement(
                    "SELECT home_name FROM homes WHERE uuid = ?")) {
                statement.setString(1, playerUUID.toString());
                ResultSet results = statement.executeQuery();

                StringBuilder homesList = new StringBuilder("Suas homes salvas: ");
                boolean hasHomes = false;
                while (results.next()) {
                    if (hasHomes) {
                        homesList.append(", ");
                    }
                    homesList.append(results.getString("home_name"));
                    hasHomes = true;
                }

                if (!hasHomes) {
                    homesList.append(" none.");
                }

                player.sendMessage(homesList.toString());
            } catch (Exception e) {
                e.printStackTrace();
                player.sendMessage("Um erro ocorreu ao recuperar suas homes.");
            }
            return true;
        }

        return false;
    }
}
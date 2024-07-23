<p align="center">
    <em>
    </em>
</p>

<h1 align="center">
	    Home Plugin
</h1>
<p align="center">
  <img src="https://github.com/user-attachments/assets/eb67f5bb-0081-4e0d-b720-7b5937b1957d" alt="animated" />
</p>


<div>
    <p align="center">
        <em>
            (Java, Minecraft 1.21)<br>
            <br>
        </em>
    <a href="https://www.linkedin.com/in/juliohf/" target="_blank">
        <img src="https://img.shields.io/static/v1?label=Author&message=Julio&color=00ba6d&style=for-the-badge&logo=LinkedIn" alt="Author: Julio">
    </a>
  <br>    
    <a href="#">
		<img  src="https://img.shields.io/static/v1?label=Language&message=Java&color=red&style=for-the-badge&logo=Java"  alt="Language: Java">
	</a>
    </p>
</div>

## 📖Índice

<p align="center">
 <a href="#descrição">Descrição</a> •
 <a href="#funcionalidades">Funcionalidades</a> •
 <a href="#instalação">Instalação</a> • 
 <a href="#uso">Uso</a> • 
 <a href="#desenvolvimento">Desenvolvimento</a> • 
<a href="#contribuição">Contribuição</a> • 
 <a href="#licença">Licença</a>
</p>

## 🗒️Descrição

<div>
    <p align="left">
    <em>
 HomePlugin é um plugin para Minecraft desenvolvido usando a API Bukkit. Ele permite que os jogadores salvem, 
teleportem e gerenciem locais específicos chamados "homes". 
As informações de localização das homes são armazenadas em um banco de dados MySQL.
    </em>
    </p>
</div>

## 💡Funcionalidades

 <p align="left">
    <em>
•	Salvar Home: Comando /sethome <nome> para salvar a localização atual. <br>
•	Teleportar para Home: Comando /home <nome> para se teleportar para uma home salva.<br>
•	Deletar Home: Comando /delhome <nome> para deletar uma home salva.<br>
•	Listar Homes: Comando /homes para listar todas as homes salvas.<br>
•	Efeito de Partícula: Efeito visual ao teleportar, configurável no arquivo de configuração.<br>
•	Cooldown: Tempo de espera configurável entre usos do comando /home.<br>
 </p>

## 📔Instalação

**Pré-requisitos** <br>
•	Servidor Minecraft com Bukkit ou Spigot. <br>
•	MySQL Server.

**Passos para Instalação**
1.	Baixe o Plugin:
o	Compile o código ou baixe o JAR do plugin a partir das Releases.
2.	Coloque o JAR no Servidor:
o	Coloque o arquivo JAR do plugin na pasta plugins do seu servidor Minecraft.
3.	Configuração do Banco de Dados:
o	Crie um banco de dados MySQL.
o	Atualize as configurações de conexão com o banco de dados no arquivo config.yml do plugin.
4.	Configuração do Plugin:
o	Inicie o servidor para gerar o arquivo config.yml na pasta plugins/HomePlugin.
o	Edite config.yml conforme necessário (configuração de cooldown e efeito de partícula).

**Exemplo de config.yml** <br>
yaml<br>
(Colocar tempo em segundos)<br>
cooldown: 60 <br>
(Colocar a partícula desejada)<br>
particle-effect: FLAME <br>

database:<br>
  host: localhost<br>
  port: 3306<br>
  database: minecraft<br>
  user: root<br>
  password: root<br>


### 🎮Uso

Comandos <br>
•	/sethome <nome> <br>
-	Salva a localização atual com o nome fornecido.<br>
-	Exemplo: /sethome minhaCasa<br>

•	/home <nome> <br>
-	Teleporta para a home salva com o nome fornecido.<br>
-	Exemplo: /home minhaCasa <br>

•	/delhome <nome> <br>
-	Deleta a home salva com o nome fornecido. <br>
-	Exemplo: /delhome minhaCasa <br>

•	/homes <br>
-	Lista todas as homes salvas. <br>

## 🖥️Desenvolvimento

Requisitos
•	JDK 8 ou superior.
•	Maven. <br>

Compilação
1.	Clone o repositório:
bash
Copiar código
git clone https://github.com/seu-usuario/HomePlugin.git
cd HomePlugin
2.	Compile o projeto usando Maven:
bash
Copiar código
mvn clean package
3.	O arquivo JAR gerado estará na pasta target.

## 📚Contribuição

1.	Fork o projeto.
2.	Crie um branch para sua feature (git checkout -b feature/branch).
3.	Commit suas mudanças (git commit -am 'Add some commit').
4.	Push para o branch (git push origin feature/branch).
5.	Crie um novo Pull Request.


## 📃Licença

<p align="center">

Released in 2024.

This project is under the [MIT license](https://github.com/Juliohf/home-plugin/blob/main/LICENSE).

Made with love by [Julio França](https://www.linkedin.com/in/juliohf/)💌.
Contact juliohcf@hotmail.com

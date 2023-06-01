# 8th Assignment Report

![](https://github.com/kianaghamsari/Second-Assignment/blob/develop/uni.png)

## Kiana Ghamsari - 400222079


# Introduction

The purpose of this project is to simulate a popular application called Steam which is for playing and downloading games, using socket programming including Server & Client, and database.


# Design and Implementation

The project includes three packages:

1: `Client`: Storing any file designed for the Client-side
2: `Server`: Storing any file designed for the Server-side
3: `Shared`: Storing classes that are used by both sides

* The simulation utilizes images as representations of games, which are stored on the server. Each game image is accompanied by a unique text file containing relevant game information. The server connects to a PostgreSQL database, importing game data on startup and using a single connection for communication throughout its runtime. Multi-threading is employed to handle multiple client connections simultaneously.

* Communication between the server and clients is facilitated by two classes: `Request` and `Response`. Clients instantiate a Request object, which is sent to the server via a web socket. The server utilizes the Response class to send data back to the client through the web socket.


# Conclusion

The Steam Gaming Service Simulation project provided valuable insights into Java programming, multi-threading concepts, and web sockets.
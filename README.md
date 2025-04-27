# 🧩 lcu-client-api

Este projeto é responsável por estabelecer uma conexão local com a League Client API (LCU API) do League of Legends.
Ele expõe uma API REST para que outras aplicações possam acessar informações do client do jogo em tempo real.

🚀 Tecnologias Utilizadas

- **Java 17+**
- **HTTP Server Nativo** (`com.sun.net.httpserver.HttpServer`)
- **Manipulação de JSON:** Jackson
- **Cliente HTTP:** `java.net.http.HttpClient` configurado para aceitar qualquer certificado SSL (bypass da validação padrão)

🔧 Detalhes da Implementação
Comunicação com a LCU API é feita via HTTPS, utilizando um SSLContext personalizado que aceita todos os certificados.
Esse mecanismo é necessário, pois o League Client gera certificados que não são reconhecidos por autoridades públicas.

A implementação do TrustAnyTrustManager e SSLContextFactory permite que o HttpClient ignore verificações de certificado durante as requisições.

```java
public class TrustAnyTrustManager implements X509TrustManager {
    @Override
    public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
    }

    @Override
    public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
    }

    @Override
    public X509Certificate[] getAcceptedIssuers() {
        return null;
    }
}

public class SSLContextFactory {
    public static SSLContext createTrustAllSSLContext() {
        SSLContext context = null;
        try {
            context = SSLContext.getInstance("TLS");
            context.init(null, new TrustManager[]{new TrustAnyTrustManager()}, new SecureRandom());
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            throw new RuntimeException(e);
        }
        return context;
    }
}

```
O HttpClient é criado da seguinte forma:

```java
HttpClient client = HttpClient.newBuilder()
    .sslContext(SSLContextFactory.createTrustAllSSLContext())
    .build();

```

## 🛠️ Funcionalidades Implementadas

- Conexão automática ao League Client.
- Exposição de endpoints HTTP locais para:
  - Estado do jogo.
  - Informações do lobby e jogadores.
  - Modos de jogo disponíveis.
- Conexão ao WebSocket da Riot Games utilizando a biblioteca `org.java_websocket.client.WebSocketClient`, permitindo:
  - Inscrição e escuta de **todos os eventos** emitidos pelo client do League of Legends.
  - Reação em tempo real a mudanças como criação de lobby, início de partidas, alterações no status dos jogadores, entre outros.

## ✨ Melhorias Futuras
- Adição de novos endpoints da LCU para expandir a quantidade de dados disponíveis (ex.: informações de partidas ranqueadas, histórico de partidas, personalização de runas).
- Refatoração da estrutura do projeto visando maior modularização, clareza e escalabilidade.
- Melhor tratamento de reconexão automática ao WebSocket em caso de quedas.
- Melhor tratamento de erros e respostas HTTP, retornando mensagens mais amigáveis.


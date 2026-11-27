# 🚀 Ecossistema Agendador de Tarefas — Microsserviços, Cibersegurança & Agente de IA

![Java](https://shields.io)
![Spring Boot](https://shields.io)
![Spring AI](https://shields.io)
![Spring Security](https://shields.io)
![PostgreSQL](https://shields.io)
![MongoDB](https://shields.io)
![Docker](https://shields.io)

API Gateway / **BFF (Backend for Frontend)** desenvolvido em **Java 17** e **Spring Boot**, responsável por orquestrar um ecossistema distribuído de microsserviços para gestão de usuários, consulta de CEP, agendamento de tarefas, disparos automáticos de notificações por e-mail e **agendamento autônomo via Agente de Inteligência Artificial Generativa (Spring AI + Google Gemini)**.

Além das regras de negócio, este projeto atua como um **laboratório prático de DevSecOps e Cibersegurança**, implementando defesa em profundidade, política CORS estrita, auditoria forense de requisições e armadilhas ativas (*Honeypots*).

---

## 🏗️ Arquitetura do Sistema

Toda a infraestrutura é containerizada e isolada em uma rede interna Docker (`backend-net`), expondo apenas a porta de entrada do BFF (`8083`) para o exterior:

```text
[ Cliente / Swagger UI ]
          │
          ▼ (Porta 8083 - Única exposta)
┌──────────────────────────────────────────────────────────┐
│                  BFF-AGENDADOR-TAREFAS                   │
│  • CorsFilter (Whitelist Explícita de Origens)           │
│  • SecurityAuditFilter (Log Forense + Anti-CRLF)         │
│  • HoneypotController (Detecção de Scanners/Invasão)     │
│  • JwtFilter (Validação Stateless HS512)                 │
│  • AssistenteController + AgendaTools (Spring AI @Tool)  │──► [ Google Gemini API ]
│  • CronService (@Scheduled a cada 5 min)                 │
└──────┬──────────────────────┬─────────────────────┬──────┘
       │ OpenFeign            │ OpenFeign           │ OpenFeign
       ▼ (8080)               ▼ (8081)              ▼ (8082)
┌──────────────┐       ┌──────────────┐      ┌──────────────┐
│   USUARIO    │       │  AGENDADOR   │      │ NOTIFICACAO  │
│  (Java/JPA)  │       │ (Java/Mongo) │      │ (Java/SMTP)  │
└──────┬───────┘       └──────┬───────┘      └──────┬───────┘
       │                      │                     │
       ▼                      ▼                     ▼
 [ PostgreSQL ]          [ MongoDB ]         [ Gmail SMTP ]
```

### Microsserviços Integrados
1. **`bff-agendador-tarefas` (Porta `8083`):** Orquestrador central, validação de tokens JWT, integração com ViaCEP, rotina agendada (`CronService`), **Agente de IA (`Spring AI`)** e camada de defesa/auditoria.
2. **`usuariogradlegroovy` (Interna `8080`):** Cadastro, autenticação, geração de tokens JWT, criptografia de senhas (`BCrypt`) e persistência relacional no **PostgreSQL**.
3. **`agendador-tarefas` (Interna `8081`):** Gerenciamento do ciclo de vida das tarefas (`PENDENTE` / `NOTIFICADO`) com persistência orientada a documentos no **MongoDB**.
4. **`notificacao` (Interna `8082`):** Serviço dedicado ao envio transacional de e-mails via protocolo SMTP.

---

## 🤖 Arquitetura do Agente de IA Generativa (UML & Fluxo de Dados)

O endpoint `POST /assistente/chat` transforma solicitações em linguagem natural (ex: *"Agende início de férias para segunda-feira às 12:00"*) em ações reais no banco de dados utilizando **Function Calling (`@Tool`)** com o modelo **`gemini-4-hydrogen`**.

### Diagrama UML de Sequência (Execução Agêntica)

```text
┌─────────┐      ┌─────────────────┐      ┌─────────────────┐      ┌──────────────────┐      ┌───────────────────┐
│ Cliente │      │Filtros Security │      │AssistenteCtrl + │      │Google Gemini API │      │ AgendadorService  │
│(Swagger)│      │(CORS/Audit/JWT) │      │ChatClient/Tools │      │(gemini-4-hydrogen│      │ (Feign + MongoDB) │
└────┬────┘      └────────┬────────┘      └────────┬────────┘      └────────┬─────────┘      └─────────┬─────────┘
     │                    │                        │                        │                          │
     │ 1. POST /assistente/chat                    │                        │                          │
     │    + Bearer Token + Prompt                  │                        │                          │
     ├───────────────────►│                        │                        │                          │
     │                    │ 2. Valida CORS, Audita │                        │                          │
     │                    │    IP & JWT (HS512)    │                        │                          │
     │                    ├───────────────────────►│                        │                          │
     │                    │                        │ 3. Vincula AgendaTools │                          │
     │                    │                        │    ao Token JWT atual  │                          │
     │                    │                        │                        │                          │
     │                    │                        │ 4. Prompt + Esquema    │                          │
     │                    │                        │    das @Tools (HTTPS)  │                          │
     │                    │                        ├───────────────────────►│                          │
     │                    │                        │                        │                          │
     │                    │                        │ 5. Tool Call:          │                          │
     │                    │                        │    obterDataHoraAtual()│                          │
     │                    │                        │◄───────────────────────┤                          │
     │                    │                        │                        │                          │
     │                    │                        │ 6. Data Atual          │                          │
     │                    │                        │    (dd-MM-yyyy HH:mm)  │                          │
     │                    │                        ├───────────────────────►│                          │
     │                    │                        │                        │                          │
     │                    │                        │ 7. Tool Call:          │                          │
     │                    │                        │    criarTarefa(dados)  │                          │
     │                    │                        │◄───────────────────────┤                          │
     │                    │                        │                        │                          │
     │                    │                        │ 8. POST /tarefas (OpenFeign + JWT)                │
     │                    │                        ├──────────────────────────────────────────────────►│
     │                    │                        │                        │                          │ 9. Grava
     │                    │                        │                        │                          │    MongoDB
     │                    │                        │ 10. TarefaDTOResponse (ID + Data)                 │
     │                    │                        │◄──────────────────────────────────────────────────┤
     │                    │                        │                        │                          │
     │                    │                        │ 11. Retorno da Tool    │                          │
     │                    │                        ├───────────────────────►│                          │
     │                    │                        │                        │                          │
     │                    │                        │ 12. Resposta Final     │                          │
     │                    │                        │     em Português       │                          │
     │                    │                        │◄───────────────────────┤                          │
     │                    │                        │                        │                          │
     │ 13. HTTP 200 OK + Confirmação Formatada     │                        │                          │
     │◄───────────────────┴────────────────────────┘                        │                          │
```

### Argumentação Técnica do Fluxo de Dados

1. Barreira de Borda e Zero Trust (Passos 1 a 3): Antes de qualquer consumo de cota de IA, a requisição é validada por CorsFilter, 
   SecurityAuditFilter e JwtFilter. O AssistenteController instancia AgendaTools vinculando-o exclusivamente ao tokenJwt da sessão atual, 
   impedindo que tentativas de Prompt Injection criem ou consultem tarefas em nome de terceiros (Prevenção contra IDOR).
2. Aterramento Temporal (Temporal Grounding, Passos 4 a 6): Como modelos de linguagem não possuem relógio interno em tempo real, 
   o agente invoca obterDataHoraAtual() antes de criarTarefa(), eliminando alucinações de calendário e calculando expressões 
   relativas ("amanhã", "segunda-feira") de forma determinística.
3. Desacoplamento via BFF e Persistência Poliglota (Passos 7 a 10): O AgendaTools não acessa o banco diretamente; 
   ele delega ao TarefaService via OpenFeign, garantindo que todas as validações de negócio e o ciclo posterior de disparo 
   de e-mails (CronService + notificacao-service) ocorram normalmente.
4. Resiliência e Tratamento Defensivo (Passos 11 a 13): A normalização de formatos de data e o tratamento explícito 
   de exceções dentro das ferramentas evitam ciclos repetitivos de chamada (tool loops) e 
   devolvem feedback claro ao usuário.
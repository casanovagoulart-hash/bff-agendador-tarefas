#  Ecossistema Agendador de Tarefas — Arquitetura de Microsserviços & Cibersegurança

API Gateway / **BFF (Backend for Frontend)** desenvolvido em **Java 17** e **Spring Boot**, responsável por orquestrar um ecossistema distribuído de microsserviços para gestão de usuários, consulta de CEP, agendamento de tarefas e disparos automáticos de notificações por e-mail.

Além das regras de negócio, este projeto atua como um **laboratório prático de DevSecOps e Cibersegurança**, implementando defesa em profundidade, auditoria forense de requisições e armadilhas ativas (*Honeypots*).

---

## 🏗️ Arquitetura do Sistema

Toda a infraestrutura é containerizada e isolada em uma rede interna Docker (`backend-net`), expondo apenas a porta de entrada do BFF (`8083`) para o exterior:

```text
[ Cliente / Swagger UI ]
          │
          ▼ (Porta 8083 - Única exposta)
┌──────────────────────────────────────────────────────────┐
│                  BFF-AGENDADOR-TAREFAS                   │
│  • SecurityAuditFilter (Log Forense + Anti-CRLF)         │
│  • HoneypotController (Detecção de Scanners/Invasão)     │
│  • JwtFilter (Validação Stateless HS512)                 │
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
1. **`bff-agendador-tarefas` (Porta `8083`):** Orquestrador central, validação de tokens JWT, integração com ViaCEP, rotina agendada (`CronService`) e camada de defesa/auditoria.
2. **`usuariogradlegroovy` (Interna `8080`):** Cadastro, autenticação, geração de tokens JWT, criptografia de senhas (`BCrypt`) e persistência relacional no **PostgreSQL**.
3. **`agendador-tarefas` (Interna `8081`):** Gerenciamento do ciclo de vida das tarefas (`PENDENTE` / `NOTIFICADO`) com persistência orientada a documentos no **MongoDB**.
4. **`notificacao` (Interna `8082`):** Serviço dedicado ao envio transacional de e-mails via protocolo SMTP.

---

## ️ Stack Tecnológica e Linguagens

* **Linguagens & Scripts:** Java 17, Groovy (Gradle DSL), SQL, PowerShell/Bash.
* **Frameworks:** Spring Boot (3.x / 4.x), Spring Security, Spring Cloud OpenFeign, Spring Data JPA, Spring Data MongoDB.
* **Bancos de Dados:** PostgreSQL 16 (Relacional) e MongoDB 7 (NoSQL).
* **Infraestrutura & Build:** Docker, Docker Compose (Multi-stage builds com cache persistente), Maven e Gradle.
* **Documentação:** SpringDoc OpenAPI 3 (Swagger UI).

---

## ️ Camadas de Cibersegurança Implementadas

* **Autenticação Stateless JWT (HS512):** Assinatura criptográfica HMAC-SHA512 utilizando chave mestre compartilhada de 512+ bits (128 caracteres Base64), conforme a **RFC 7518**, e tokens com expiração curta (1 hora).
* **Prevenção contra IDOR (*Insecure Direct Object Reference*):** Endpoints sensíveis extraem a identidade do usuário diretamente do *Subject* do token JWT validado, impedindo manipulação de IDs ou e-mails via URL.
* **Filtro de Auditoria Forense (`SecurityAuditFilter`):** Intercepta tentativas de acesso não autorizado (`401`) e proibido (`403`), registrando IP real de origem (`X-Forwarded-For`), rota alvo, credencial apresentada e `User-Agent`, com sanitização contra **Log Injection (CRLF)**.
* **Armadilha Ativa (`HoneypotController`):** Endpoints isca ocultos da documentação (`@Hidden` em `/admin`, `/api/admin/backup`, `/.env`, `/actuator/env`) para detectar e registrar assinaturas de bots e scanners automatizados.
* **Gestão Segura de Segredos (OPSEC):** Externalização total de credenciais via variáveis de ambiente (`.env` bloqueado no `.gitignore` + template seguro `.env.example`).

---

##  Como Executar o Projeto (Docker Compose)

### 1. Pré-requisitos
* **Docker Desktop** instalado e em execução.
* Repositórios irmãos clonados no mesmo diretório pai (`bff-agendador-tarefas`, `usuariogradlegroovy`, `agendador-tarefas`, `notificacao`).

### 2. Configurar Variáveis de Ambiente
Na raiz do `bff-agendador-tarefas`, copie o arquivo de modelo para `.env`:
```powershell
Copy-Item .env.example .env
```

Gere uma chave secreta criptograficamente segura no PowerShell para a variável `JWT_SECRET`:
```powershell
\$bytes = New-Object byte[] 96; [Security.Cryptography.RandomNumberGenerator]::Create().GetBytes(\(bytes); [Convert]::ToBase64String(\)bytes)
```
Preencha o `.env` com a chave gerada, a senha do PostgreSQL, as credenciais SMTP do Gmail e o usuário do robô `CronService`.

### 3. Subir os Containers
```powershell
docker compose up -d --build
```

### 4. Acessar a Documentação Interativa (Swagger UI)
Abra no navegador:
 **`http://localhost:8083/swagger-ui/index.html`**

---

##  Documentações Oficiais, Referências e Colaboração Técnica

O desenvolvimento, diagnóstico e endurecimento (*hardening*) deste ecossistema contaram com as seguintes bases de conhecimento e suporte técnico:

* **Assistência Arquitetural e Pair Programming com IA (Google):** Principal fonte de apoio técnico e diagnóstico contínuo durante todo o ciclo de desenvolvimento — desde a revisão de contratos DTO/OpenFeign, migração de propriedades no Spring Boot 4 e otimização de builds Docker, até a implementação das camadas de DevSecOps (`SecurityAuditFilter`, `HoneypotController` e proteção contra *Log Injection*).
* **Spring Security & Spring Boot Reference Documentation:** Configuração de cadeias de filtros stateless (`SecurityFilterChain`), `OncePerRequestFilter`, ordem de precedência de filtros e propriedades de conexão (`spring.mongodb.uri`).
* **Spring Cloud OpenFeign Documentation:** Comunicação declarativa entre microsserviços, propagação de cabeçalhos `Authorization` e tratamento de exceções HTTP via `ErrorDecoder`.
* **MongoDB & Spring Data MongoDB Docs:** Modelagem de documentos, consultas por intervalo de datas (`LocalDateTime`) e conectividade em redes internas Docker.
* **IETF RFC 7518 (JSON Web Algorithms) & JJWT Documentation:** Requisitos mínimos de tamanho de chave (≥ 512 bits) para algoritmos HMAC-SHA512 e prevenção contra `WeakKeyException`.
* **OWASP Cheat Sheet Series (Logging, JWT & IDOR):** Boas práticas para ocultação de hashes de senha nos DTOs de resposta, sanitização de logs contra injeção de quebras de linha e controle de acesso baseado no token.
* **Docker & Compose Official Docs:** Construção otimizada com *Multi-Stage Builds*, cache de dependências (`--mount=type=cache`) e isolamento de redes *bridge*.
* **Comunidade Stack Overflow:** Consulta de cenários práticos envolvendo precedência de variáveis de ambiente no Windows/Docker, mapeamento Jackson/Lombok e diagnóstico de rede entre containers.

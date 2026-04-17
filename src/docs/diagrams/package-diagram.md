# 📦 Diagrama de Pacotes — Car Rental System

> **Versão:** 2.0 · **Sprint:** 02 — Revisão pós-feedback + alinhamento com implementação  
> **Arquitetura:** Micronaut · MVC em Camadas · Clean Architecture  
> **Notação:** UML 2.5 · **Formato:** Mermaid (ISO/IEC 19501 compliant)  
> **Renderização nativa:** GitHub, GitLab, Azure DevOps, Confluence, Notion

---

## Changelog (v1.0 → v2.0)

| Alteração | Motivo |
|-----------|--------|
| Adicionado `HomeController` ao pacote `controller` | Ponto de entrada raiz `/` com redirect |
| Adicionado DTOs de endereço e empregador (`AddressDTO`, `EmployerDTO`) | Formulários de CRUD de cliente necessitam DTOs aninhados |
| Adicionado pacote `exception` com classes concretas | Implementação do `GlobalExceptionHandler` com `@Error (Micronaut)` |
| Dependência `MAPPER → DTO` explicitada | O Mapper transforma bidireccionalmente Entity ↔ DTO |
| Alinhamento com estrutura real de pacotes Java implementada | Diagrama reflete o código fonte — diagrama como código |

---

## Visão Geral da Arquitetura de Pacotes

```mermaid
block-beta
    columns 1

    block:presentation["🌐 CAMADA DE APRESENTAÇÃO — controller"]
        columns 7
        HomeController AuthController ClientController AgentController CarController RentalRequestController RentalContractController
    end

    space

    block:application["⚙️ CAMADA DE APLICAÇÃO — service"]
        columns 7
        AuthService ClientService AgentService CarService RentalRequestService RentalContractService CreditContractService
    end

    space

    block:domain["🏛️ CAMADA DE DOMÍNIO — domain"]
        columns 4
        block:entity["entity"]
            columns 3
            User Client Agent
            Company Bank Car
            RentalRequest RentalContract CreditContract
            Employer Address
        end
        block:enums["enums"]
            UserRole RequestStatus
        end
        block:dto["dto"]
            columns 2
            ClientRequestDTO ClientResponseDTO
            AddressDTO EmployerDTO
            CarDTO RentalRequestDTO
            AuthRequestDTO AuthResponseDTO
        end
        block:mapper["mapper"]
            ClientMapper CarMapper
            RentalRequestMapper
        end
    end

    space

    block:infra["💾 CAMADA DE INFRAESTRUTURA — repository"]
        columns 7
        UserRepository ClientRepository AgentRepository CarRepository RentalRequestRepository RentalContractRepository CreditContractRepository
    end

    space

    block:cross1["🔧 CROSS-CUTTING — config"]
        columns 3
        SecurityConfig WebMvcConfig PersistenceConfig
    end

    block:cross2["🚨 CROSS-CUTTING — exception"]
        columns 3
        GlobalExceptionHandler ResourceNotFoundException BusinessRuleException
    end

    presentation --> application
    application --> domain
    application --> infra
    infra --> entity

    style presentation fill:#DCEDC8,stroke:#558B2F,color:#1B5E20
    style application fill:#FFF9C4,stroke:#F9A825,color:#E65100
    style domain fill:#FCE4EC,stroke:#C62828,color:#B71C1C
    style entity fill:#F8BBD0,stroke:#AD1457
    style enums fill:#E1BEE7,stroke:#7B1FA2
    style dto fill:#C5CAE9,stroke:#283593
    style mapper fill:#D1C4E9,stroke:#4527A0
    style infra fill:#E0F7FA,stroke:#00838F,color:#004D40
    style cross1 fill:#FBE9E7,stroke:#BF360C
    style cross2 fill:#F9FBE7,stroke:#827717
```

---

## Dependências entre Pacotes

```mermaid
flowchart TB
    subgraph PRESENTATION["🌐 controller"]
        direction LR
        HC[HomeController]
        AC[AuthController]
        CC[ClientController]
        AGC[AgentController]
        CarC[CarController]
        RRC[RentalRequestController]
        RCC[RentalContractController]
    end

    subgraph APPLICATION["⚙️ service"]
        direction LR
        AS[AuthService]
        CS[ClientService]
        AGS[AgentService]
        CarS[CarService]
        RRS[RentalRequestService]
        RCS[RentalContractService]
        CCS[CreditContractService]
    end

    subgraph DOMAIN["🏛️ domain"]
        direction TB
        subgraph ENTITY["entity"]
            direction LR
            User & Client & Agent
            Company & Bank & Car
            RentalRequest & RentalContract & CreditContract
            Employer & Address
        end
        subgraph ENUMS["enums"]
            UserRole & RequestStatus
        end
        subgraph DTO["dto"]
            direction LR
            ClientReqDTO[ClientRequestDTO]
            ClientResDTO[ClientResponseDTO]
            AddressDTO
            EmployerDTO
            CarDTO
            RentalReqDTO[RentalRequestDTO]
            AuthReqDTO[AuthRequestDTO]
            AuthResDTO[AuthResponseDTO]
        end
        subgraph MAPPER["mapper"]
            ClientMapper & CarMapper & RentalRequestMapper
        end
    end

    subgraph INFRASTRUCTURE["💾 repository"]
        direction LR
        UR[UserRepository]
        CR[ClientRepository]
        AGR[AgentRepository]
        CarR[CarRepository]
        RRR[RentalRequestRepository]
        RCR[RentalContractRepository]
        CCR[CreditContractRepository]
    end

    subgraph CONFIG["🔧 config"]
        direction LR
        SC[SecurityConfig]
        WMC[WebMvcConfig]
        PC[PersistenceConfig]
    end

    subgraph EXCEPTION["🚨 exception"]
        direction LR
        GEH[GlobalExceptionHandler]
        RNF[ResourceNotFoundException]
        BRE[BusinessRuleException]
    end

    %% ═══ Dependências entre camadas (fluxo unidirecional) ═══

    PRESENTATION -- "«use» delega operações" --> APPLICATION
    APPLICATION -- "«use» opera entidades/DTOs" --> DOMAIN
    APPLICATION -- "«use» persiste/consulta" --> INFRASTRUCTURE
    INFRASTRUCTURE -- "«use» gerencia entidades JPA" --> ENTITY
    PRESENTATION -- "«use» tratamento de erros" --> EXCEPTION
    APPLICATION -- "«use» lança exceções" --> EXCEPTION
    CONFIG -. "«configure»" .-> APPLICATION
    MAPPER -- "«transform»" --> ENTITY
    MAPPER -- "«transform»" --> DTO

    %% ═══ Estilos ═══

    style PRESENTATION fill:#DCEDC8,stroke:#558B2F,color:#1B5E20
    style APPLICATION fill:#FFF9C4,stroke:#F9A825,color:#E65100
    style DOMAIN fill:#FCE4EC,stroke:#C62828,color:#B71C1C
    style ENTITY fill:#F8BBD0,stroke:#AD1457
    style ENUMS fill:#E1BEE7,stroke:#7B1FA2
    style DTO fill:#C5CAE9,stroke:#283593
    style MAPPER fill:#D1C4E9,stroke:#4527A0
    style INFRASTRUCTURE fill:#E0F7FA,stroke:#00838F,color:#004D40
    style CONFIG fill:#FBE9E7,stroke:#BF360C
    style EXCEPTION fill:#F9FBE7,stroke:#827717
```

---

## Regra de Dependência

```mermaid
flowchart LR
    A["🌐 Controller<br/><i>Presentation</i>"] --> B["⚙️ Service<br/><i>Application</i>"]
    B --> C["🏛️ Domain<br/><i>Entities · DTOs</i>"]
    B --> D["💾 Repository<br/><i>Infrastructure</i>"]
    D --> C

    E["🔧 Config"] -.-> B
    F["🚨 Exception"] -.-> A
    F -.-> B

    style A fill:#DCEDC8,stroke:#558B2F
    style B fill:#FFF9C4,stroke:#F9A825
    style C fill:#FCE4EC,stroke:#C62828
    style D fill:#E0F7FA,stroke:#00838F
    style E fill:#FBE9E7,stroke:#BF360C
    style F fill:#F9FBE7,stroke:#827717
```

> **Regra de ouro:** cada camada só conhece a camada imediatamente abaixo — **nunca acima**.  
> A dependência flui de fora para dentro, conforme prescrito pela *Clean Architecture* (Martin, 2017).

---

## Fluxo de Dados HTTP

```mermaid
sequenceDiagram
    participant Browser as 🌐 Browser
    participant Controller as Controller
    participant Service as Service
    participant Mapper as Mapper
    participant Repository as Repository
    participant DB as 💾 Database

    Browser->>+Controller: HTTP Request (GET/POST)
    Note over Browser,Controller: TLS 1.3 · TCP/HTTP 1.1

    Controller->>Controller: @Valid — Bean Validation (JSR-380)
    Controller->>+Service: delega operação (DTO)

    Service->>+Mapper: converte DTO → Entity
    Mapper-->>-Service: Entity

    Service->>Service: aplica regras de negócio
    Service->>+Repository: persiste/consulta
    Repository->>+DB: SQL (Hibernate ORM)
    DB-->>-Repository: ResultSet
    Repository-->>-Service: Entity

    Service->>+Mapper: converte Entity → ResponseDTO
    Mapper-->>-Service: ResponseDTO

    Service-->>-Controller: ResponseDTO

    Controller-->>-Browser: HTTP Response (HTML/JSON)
    Note over Browser,Controller: ModelAndView ou ResponseEntity
```

---

## Notas Arquiteturais

### Por que MVC em Camadas?

| Decisão | Justificativa |
|---------|---------------|
| **Controller sem lógica** | SRP (Single Responsibility Principle) — o Controller apenas roteia HTTP e delega ao Service. O mesmo caso de uso pode ser invocado via HTTP, filas ou testes. |
| **Service como Facade** | Padrão Facade (GoF) — orquestra casos de uso, controla transações `@Transactional` e implementa a máquina de estados. |
| **Repository como abstração** | Padrão Repository (DDD — Evans) — isola o domínio do mecanismo de persistência. Swapable: H2 ↔ PostgreSQL sem impacto. |
| **DTOs separados de Entities** | Evita over-fetching, coupling e vulnerabilidades de mass assignment. DTOs são contratos de API versionáveis. |
| **Mapper dedicado** | Responsabilidade única de transformação Entity ↔ DTO. Facilita testes e evolução independente. |

### Pacotes Cross-Cutting

- **`config/`** — Configurações transversais: `SecurityConfig` (Micronaut Security + CSRF), `WebMvcConfig` (Thymeleaf, CORS), `PersistenceConfig` (DataSource, JPA).
- **`exception/`** — Tratamento centralizado via `@Error (Micronaut)`. Mapeia exceções de domínio → HTTP status codes (RFC 7807 — Problem Details for HTTP APIs).

---

## Ferramentas de Visualização

| Ferramenta | Suporte Nativo | Uso Corporativo |
|-----------|---------------|-----------------|
| **GitHub** | ✅ Renderiza `.md` com Mermaid | Microsoft, Google, Amazon |
| **GitLab** | ✅ Nativo desde v 15.0 | Fortune 500 |
| **Azure DevOps** | ✅ Wiki e Repos | Enterprises Microsoft stack |
| **Confluence** | ✅ Via plugin Mermaid | Atlassian ecosystem |
| **Notion** | ✅ Blocos de código Mermaid | Startups e scale-ups |
| **VS Code** | ✅ Markdown Preview Mermaid | Qualquer desenvolvedor |
| **IntelliJ IDEA** | ✅ Plugin Mermaid | JetBrains ecosystem |


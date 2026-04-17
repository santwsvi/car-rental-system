# 🤝 Guia de Contribuição — Car Rental System

Obrigado por investir seu tempo neste projeto! Este guia estabelece os padrões de colaboração,
o fluxo de trabalho, as convenções de código e os critérios de qualidade que toda contribuição
deve seguir. Leia-o na íntegra antes de abrir sua primeira Issue ou Pull Request.

> **Princípio geral:** código é lido muito mais do que escrito. Escreva para o próximo
> desenvolvedor (que pode ser você daqui a seis meses). — *Robert C. Martin, Clean Code*

---

## 📚 Índice

- [Código de Conduta](#-código-de-conduta)
- [Fluxo de Contribuição](#-fluxo-de-contribuição)
- [Estratégia de Branches](#-estratégia-de-branches)
- [Convenção de Mensagens de Commit](#-convenção-de-mensagens-de-commit-conventional-commits)
- [Padrões de Código](#-padrões-de-código)
  - [Java · Micronaut](#java--micronaut)
  - [Arquitetura e Camadas](#arquitetura-e-camadas)
  - [Banco de Dados e JPA](#banco-de-dados-e-jpa)
  - [Segurança](#segurança)
- [Testes](#-testes)
- [Diagramas UML](#-diagramas-uml)
- [Checklist do Pull Request](#-checklist-do-pull-request)
- [Processo de Review](#-processo-de-review)
- [Rituais de Sprint](#-rituais-de-sprint)

---

## 🧭 Código de Conduta

Este projeto adota o [Contributor Covenant v2.1](https://www.contributor-covenant.org/version/2/1/code_of_conduct/).
Resumidamente: seja respeitoso, construtivo e colaborativo. Discussões técnicas são bem-vindas;
ataques pessoais não.

---

## 🚀 Fluxo de Contribuição

```
Issue (opcional)
    │
    ▼
Fork → Clone
    │
    ▼
Branch a partir de `develop`
    │
    ▼
Implementação + Testes + Diagrama (se aplicável)
    │
    ▼
mvn clean verify  (todos os testes devem passar)
    │
    ▼
Commit (Conventional Commits)
    │
    ▼
Push → Pull Request para `develop`
    │
    ▼
Code Review (mínimo 1 aprovação)
    │
    ▼
Merge (Squash & Merge para histórico limpo)
```

### Passo a passo

1. **Abra uma Issue** *(recomendado para features e bugs)* — descreva o problema/proposta antes de codificar.
2. **Fork e Clone:**
   ```bash
   git clone https://github.com/seu-usuario/car-rental-system.git
   cd car-rental-system
   ```
3. **Crie uma branch** a partir de `develop` (ver seção de branches):
   ```bash
   git checkout develop
   git pull origin develop
   git checkout -b feat/rental-request-workflow
   ```
4. **Implemente, teste e valide:**
   ```bash
   ./mvnw clean verify           # compila + testes
   ```
5. **Commit** seguindo a convenção (ver abaixo).
6. **Push e abra o PR** apontando para `develop`.

---

## 🌿 Estratégia de Branches

Utilizamos um fluxo baseado no **GitHub Flow simplificado**, adaptado ao ciclo de sprints acadêmicas:

| Branch | Finalidade | Política de merge |
|--------|-----------|-------------------|
| `main` | Código estável; reflete o estado de cada sprint entregue | Somente via PR de `develop`; requer aprovação do professor |
| `develop` | Branch de integração contínua da sprint atual | Merge dos PRs de feature/bugfix |
| `feat/<descricao>` | Nova funcionalidade | PR para `develop` |
| `fix/<descricao>` | Correção de bug | PR para `develop` |
| `refactor/<descricao>` | Melhoria de código sem mudança de comportamento | PR para `develop` |
| `docs/<descricao>` | Atualização de documentação ou diagramas | PR para `develop` |
| `chore/<descricao>` | Configuração, dependências, CI | PR para `develop` |

> **Nunca** faça push direto em `main`. Todo código entra por Pull Request.

---

## 📝 Convenção de Mensagens de Commit (Conventional Commits)

Seguimos a especificação [**Conventional Commits v1.0.0**](https://www.conventionalcommits.org/en/v1.0.0/).

### Formato

```
<tipo>(<escopo>): <descrição curta no imperativo>

[corpo opcional — explique o *porquê*, não o *o quê*]

[rodapé opcional — BREAKING CHANGE, Closes #issue]
```

### Tipos disponíveis

| Tipo | Quando usar |
|------|-------------|
| `feat` | Nova funcionalidade ou endpoint |
| `fix` | Correção de bug em código de produção |
| `docs` | Alterações em README, CONTRIBUTING, diagramas, Javadoc |
| `style` | Formatação (espaços, ponto-e-vírgula) sem mudança de lógica |
| `refactor` | Melhoria de código sem alterar comportamento externo |
| `test` | Adição ou correção de testes (sem alterar código de produção) |
| `chore` | Build, dependências, scripts CI/CD |
| `perf` | Melhoria de performance |

### Escopos sugeridos

`auth`, `client`, `agent`, `car`, `rental`, `contract`, `security`, `db`, `ui`, `config`, `diagrams`

### Exemplos válidos

```bash
feat(client): implementa CRUD de cliente com validação de CPF

fix(rental): corrige transição de estado PENDING → UNDER_REVIEW

docs(diagrams): atualiza diagrama de classes com entidade CreditContract

refactor(service): extrai lógica de validação de RentalRequestService para RentalValidator

test(client): adiciona testes de integração para ClientController

chore(pom): adiciona dependências Micronaut Data e Thymeleaf Views
```

---

## 🧹 Padrões de Código

### Java · Micronaut

- **Java 21** — use records para DTOs imutáveis, sealed classes para hierarquias fechadas, text blocks para SQL ou JSON em testes.
- **Micronaut DI:** Compile-time injection via `@Singleton`, `@Controller`, `@Repository`. Sem reflexão em runtime — startup ultra-rápido.
- **`@Introspected`:** obrigatório em DTOs e beans que Micronaut precisa introspectar em compile-time (validação, serialização, form binding).
- **Lombok:** use `@Getter`, `@Setter`, `@Builder`, `@NoArgsConstructor`, `@AllArgsConstructor` para reduzir boilerplate. Evite `@Data` em entidades JPA (gera `hashCode`/`equals` baseados em todos os campos, problemático com lazy-loading).
- **Nomenclatura:**
  - Classes: `PascalCase` (`RentalRequestService`)
  - Métodos/variáveis: `camelCase` (`findRentalRequestsByStatus`)
  - Constantes: `UPPER_SNAKE_CASE` (`MAX_EMPLOYERS_PER_CLIENT = 3`)
  - Pacotes: `lowercase` sem underscores
- **Formatação:** Google Java Style Guide (configure no seu IDE via `google-java-format`).
- **Comprimento de linha:** máximo 120 caracteres.
- **Javadoc:** obrigatório em métodos `public` de `Service` e `Controller`. Use `@param`, `@return`, `@throws`.

```java
/**
 * Submete um pedido de aluguel para revisão por um agente.
 *
 * @param requestId ID do pedido a ser submetido
 * @throws ResourceNotFoundException se o pedido não existir
 * @throws BusinessRuleException se o pedido não estiver no status PENDING
 */
public RentalRequestResponseDTO submitForReview(Long requestId) { ... }
```

### Arquitetura e Camadas

> A violação da regra de dependência entre camadas é o principal anti-pattern a evitar.

| ❌ Proibido | ✅ Correto |
|------------|-----------|
| Lógica de negócio no `@Controller` | Delegar ao `@Singleton` Service |
| `@Entity` JPA retornada diretamente no Controller | Converter para DTO via `Mapper` |
| `@Repository` injetado diretamente no `@Controller` | Injetar somente Service no Controller |
| SQL nativo na camada `Service` | Usar métodos derivados do Micronaut Data ou `@Query` JPQL no `Repository` |
| Exceção `RuntimeException` genérica | Lançar `BusinessRuleException` ou `ResourceNotFoundException` específicas |

**Injeção de dependência:** sempre via **construtor** (Micronaut compile-time DI):

```java
// ✅ Correto — injeção por construtor (Micronaut)
@Singleton
public class RentalRequestService {
    private final RentalRequestRepository repository;
    private final ClientRepository clientRepository;

    public RentalRequestService(RentalRequestRepository repository,
                                 ClientRepository clientRepository) {
        this.repository = repository;
        this.clientRepository = clientRepository;
    }
}
```

### Banco de Dados e JPA

- **DDL:** nunca use `hbm2ddl.auto=create-drop` fora do perfil `dev`. Use `validate` em `prod`.
- **N+1 Problem:** sempre que carregar coleções, use `JOIN FETCH` ou `@EntityGraph` — nunca deixe o Hibernate disparar N queries em loop.
- **Transações:** toda operação de escrita deve estar em um método `@Transactional` (`jakarta.transaction`) na camada `Service`.

```java
@Transactional
public List<RentalRequestResponseDTO> findAllByClient(Long clientId) { ... }

@Transactional
public RentalRequestResponseDTO create(RentalRequestDTO dto) { ... }
```

### Segurança

- **Senhas:** nunca armazene texto puro. Use BCrypt com `strength >= 12`.
- **PII em logs:** nunca logue CPF, RG, número de cartão ou senha — nem mesmo mascarados em nível DEBUG em produção.
- **Validação de entrada:** use Bean Validation (JSR-380) com `jakarta.validation` nos DTOs. Nunca confie em dados do cliente sem validação.

```java
@Introspected
public record ClientRequestDTO(
    @NotBlank(message = "Nome é obrigatório")
    String name,

    @NotBlank(message = "CPF é obrigatório")
    String cpf,

    @Email
    String email
) {}
```

---

## 🧪 Testes

**Regra de ouro:** todo novo código de `service/` e `domain/` deve vir acompanhado de testes.

### Estrutura esperada

```
src/test/java/com/pucminas/car_rental_system/
├── service/
│   ├── ClientServiceTest.java          ← testes unitários com Mockito
│   └── RentalRequestServiceTest.java   ← cobertura da FSM de estados
└── controller/
    ├── ClientControllerTest.java       ← @MicronautTest integration tests
    └── RentalRequestControllerTest.java
```

### Convenções de nomenclatura de testes

Use o padrão **`methodName_stateUnderTest_expectedBehavior`**:

```java
@Test
void submitForReview_whenStatusIsPending_shouldChangeStatusToUnderReview() { ... }

@Test
void submitForReview_whenStatusIsNotPending_shouldThrowBusinessRuleException() { ... }

@Test
void addEmployer_whenClientHasThreeEmployers_shouldThrowBusinessRuleException() { ... }
```

### Cobertura mínima

| Camada | Cobertura mínima exigida |
|--------|--------------------------|
| `service/` | 80% de linhas |
| `domain/entity/` | 70% de linhas |
| `controller/` | Coberto via testes de integração `@MicronautTest` |
| `exception/` | Sem exigência mínima |

Execute para verificar:
```bash
./mvnw verify
```

---

## 📐 Diagramas UML

Os diagramas são **código** — vivem no repositório e são versionados como qualquer outro artefato.  
Utilizamos **Mermaid** — o padrão de mercado para *diagrams-as-code*, renderizado nativamente pelo GitHub, GitLab, Azure DevOps, Confluence e Notion.

- `src/docs/diagrams/class-diagram.md` — Diagrama de Classes (Mermaid UML)
- `src/docs/diagrams/package-diagram.md` — Diagrama de Pacotes (Mermaid UML)
- `src/docs/diagrams/component-diagram.md` — Diagrama de Componentes (Mermaid UML)

**Regra:** ao adicionar uma nova entidade, associação ou pacote, **atualize o diagrama correspondente** no mesmo commit ou PR.

---

## ✅ Checklist do Pull Request

Antes de abrir seu PR, revise cada item:

### Código
- [ ] A implementação segue a **regra de dependência entre camadas** (Controller → Service → Repository)
- [ ] Nenhuma lógica de negócio foi adicionada no Controller
- [ ] As entidades JPA **não** são retornadas diretamente nas respostas HTTP (usar DTOs)
- [ ] Injeção de dependência feita via **construtor**
- [ ] Operações de escrita têm `@Transactional` (`jakarta.transaction`)
- [ ] Exceções específicas são lançadas (não `RuntimeException` genérica)

### Testes
- [ ] Novos serviços têm testes unitários cobrindo **casos de sucesso e falha**
- [ ] `./mvnw clean verify` executa sem erros

### Mensagem de Commit e Branch
- [ ] A mensagem de commit segue **Conventional Commits**
- [ ] A branch tem nome descritivo no formato `tipo/descricao`
- [ ] O PR aponta para `develop` (nunca para `main` diretamente)

### Documentação
- [ ] Javadoc adicionado em métodos `public` de `Service` e `Controller`
- [ ] Se foi adicionada uma nova entidade/pacote, o **diagrama UML foi atualizado**
- [ ] Se a mudança impacta a execução/configuração, o **README foi atualizado**

### Segurança e Qualidade
- [ ] Nenhum dado sensível (CPF, senha, chave) está hard-coded ou logado
- [ ] Inputs do usuário são validados com Bean Validation
- [ ] Sem código comentado desnecessário ("código morto")

---

## 👀 Processo de Review

1. **Prazo:** o revisor tem até **48 horas** para fazer o primeiro feedback.
2. **Mínimo 1 aprovação** de outro membro da equipe para habilitar o merge.
3. **Feedback construtivo:** ao apontar um problema, sugira a solução ou explique o princípio violado.
4. **Resolução de conflitos:** o autor do PR é responsável por resolver conflitos de merge contra `develop`.
5. **Merge strategy:** `Squash and Merge` para features; `Merge Commit` para merges de sprint em `main`.

---

## 📅 Rituais de Sprint

| Ritual | Frequência | Objetivo |
|--------|-----------|----------|
| **Apresentação de andamento** | Semanal (em aula) | Demonstrar progresso; grupo sorteado — ausência = -50% da sprint |
| **Code Review** | Contínuo (48h) | Garantir qualidade antes do merge |
| **Atualização dos diagramas** | A cada PR que muda o modelo | Manter alinhamento modelo ↔ código |
| **Retrospectiva de sprint** | Ao final de cada sprint | Identificar melhorias de processo |

### Datas das entregas (consulte o cronograma oficial)

- **Sprint 01:** ✅ Modelagem inicial (diagramas de classes, pacotes, casos de uso, histórias)
- **Sprint 02:** 🟡 Revisão dos diagramas (v2.0) + Diagrama de Componentes + CRUD de Cliente (Web MVC + JPA)
- **Sprint 03 (final):** Revisão dos diagramas + Diagrama de Implantação + Protótipo funcional

> 🔗 [Cronograma oficial do Prof. Aramuni](https://github.com/joaopauloaramuni/laboratorio-de-desenvolvimento-de-software/tree/main/CRONOGRAMA)

---

<div align="center">
  Dúvidas? Abra uma <a href="../../issues">Issue</a> ou mencione um colega no PR. 🚀
</div>

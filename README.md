# Sistema ERP para Barbearia

## Descrição
Sistema de gerenciamento para barbearias desenvolvido em Java Spring Boot com arquitetura MVC.

## Funcionalidades

### Para Administradores:
- Dashboard com estatísticas
- Gerenciamento de usuários (clientes e funcionários)
- Gerenciamento de serviços
- Gerenciamento de agendamentos
- Visualização completa do sistema

### Para Clientes:
- Dashboard pessoal
- Criação de novos agendamentos
- Visualização dos próprios agendamentos
- Cancelamento de agendamentos

### Para Funcionários:
- Dashboard com agenda pessoal
- Visualização de agendamentos alocados
- Controle de disponibilidade

## Tecnologias Utilizadas
- Java 17
- Spring Boot 3.2.0
- Spring Security
- Spring Data JPA
- Thymeleaf
- Bootstrap 5
- H2 Database (desenvolvimento)
- Maven

## Como Executar

1. Clone o repositório
2. Execute: `mvn spring-boot:run`
3. Acesse: `http://localhost:3000`
4. H2: `http://localhost:3000/h2-console`
    - Driver Class: org.h2.Driver
    - JDBC URL: jdbc:h2:mem:testdb 
    - User Name: barbearia 
    - Password: barbearia

## Usuário Padrão
- **Admin**: admin@barbearia.com / adimin123

## Estrutura do Banco de Dados
- usuarios (classe base)
- clientes (herda de usuarios)
- funcionarios (herda de usuarios)
- servicos
- agendamentos
- alocacao_agendamentos

## Regras de Negócio
- Funcionários não podem ser alocados em horários conflitantes
- Agendamentos só podem ser feitos em horários comerciais (8h-18h)
- Não é possível agendar aos domingos
- Clientes só podem cancelar seus próprios agendamentos
- Apenas administradores podem gerenciar usuários e serviços

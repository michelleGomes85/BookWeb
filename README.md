# E-Commerce de Livros

## Sobre o Projeto

Este projeto é um sistema web de e-commerce de livros desenvolvido em Java. Ele utiliza tecnologias como JPA, JavaServer Faces (JSF) e PrimeFaces para a construção da interface e funcionalidade. O sistema permite a autenticação de usuários, gerenciamento de livros, controle de estoque e simulação de compras.

## Funcionalidades Principais

- **Cadastro e Autenticação de Usuários**
  - Login e criação de perfis
  - Perfis de Funcionário e Administrador podem cadastrar livros e categorias
  - Usuários clientes podem adicionar livros ao carrinho e realizar pedidos
  
- **Gerenciamento de Livros e Categorias**
  - Cadastro, edição e remoção de livros e categorias
  - Controle de estoque
  
- **Carrinho de Compras e Pedidos**
  - Clientes podem adicionar livros ao carrinho
  - O sistema mantém os dados da sessão mesmo sem login
  - O usuário precisa se autenticar para finalizar o pedido
  - Simulação de compra com atualização do estoque

## Estrutura do Código

O sistema segue um modelo de entidades interligadas utilizando JPA:

### Modelos Principais

- **Book**: Representa um livro com atributos como ISBN, título, descrição, estoque, preço e categoria.
- **Category**: Representa uma categoria de livros.
- **Client**: Representa um cliente com seus dados cadastrais e pedidos.
- **Order**: Representa um pedido feito por um cliente.
- **ItemOrder**: Representa um item dentro de um pedido.
- **User**: Representa a conta de usuário para autenticação e autorização.

## Tecnologias Utilizadas

- **Linguagem:** Java
- **Frameworks:** JavaServer Faces (JSF), PrimeFaces
- **Persistência:** JPA (Jakarta Persistence API)
- **Banco de Dados:** PostgreSQL
- **Servidor de Aplicação:** Tomcat

## Como Executar o Projeto

1. Clone o repositório:
   ```bash
   git clone https://github.com/seu-repositorio.git
   ```
2. Configure o banco de dados e atualize as configurações de conexão.
3. Compile e execute o projeto utilizando um servidor compatível.
4. Acesse a aplicação via navegador para testar as funcionalidades.

## Observações

- O sistema não realiza transações financeiras reais, apenas simula compras controlando o estoque.
- O envio de e-mails é utilizado para comunicação com o usuário após o cadastro e realização de pedidos.

---

Este projeto foi desenvolvido como parte de um estudo e aprimoramento em desenvolvimento web com Java.


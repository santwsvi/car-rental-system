# Histórias de Usuário - Sistema de Aluguel de Carro

## História 1: Cadastro no Sistema
**Como** um cliente,  
**quero** efetuar o meu cadastro informando os meus dados de identificação (RG, CPF, Nome, Endereço), profissão e rendimentos,  
**para** que eu tenha acesso ao sistema e possa realizar pedidos de aluguel.  

**Testes de Aceitação:** 1. O sistema deve exigir e validar o preenchimento de RG, CPF, Nome e Endereço.  
2. O sistema deve permitir o cadastro de até 3 entidades empregadoras com os seus respetivos rendimentos auferidos.

---

## História 2: Introduzir Pedido de Aluguel
**Como** um cliente cadastrado e logado,  
**quero** introduzir um novo pedido de aluguel de automóvel,  
**para** iniciar o processo de locação.  

**Testes de Aceitação:** 1. O sistema não deve permitir a criação de pedidos por utilizadores que não realizaram o cadastro prévio.  
2. Após a introdução, o pedido deve ser encaminhado automaticamente para a fila de análise financeira dos agentes.

---

## História 3: Consultar Pedido
**Como** um cliente,  
**quero** consultar a lista dos meus pedidos de aluguel,  
**para** acompanhar o status de cada solicitação.  

**Testes de Aceitação:** 1. O cliente só pode visualizar os pedidos vinculados ao seu próprio cadastro.  
2. A interface deve exibir de forma clara se o pedido está em análise, aprovado ou a aguardar execução de contrato.

---

## História 4: Modificar Pedido 
**Como** um cliente,  
**quero** modificar as informações de um pedido de aluguel já introduzido,  
**para** corrigir dados ou alterar os termos da locação antes da aprovação.  

**Testes de Aceitação:** 1. O sistema só deve permitir a modificação de pedidos que ainda não tiveram o contrato executado.  
2. Qualquer alteração feita pelo cliente deve retornar o pedido para reavaliação dos agentes, caso já estivesse em análise.

---

## História 5: Cancelar Pedido
**Como** um cliente,  
**quero** cancelar um pedido de aluguel,  
**para** desistir da locação caso não tenha mais interesse.  

**Testes de Aceitação:** 1. O botão de cancelamento deve estar disponível apenas para contratos não executados.  
2. Ao cancelar, o status do pedido deve mudar imediatamente para "Cancelado" e interromper o processo de análise financeira.

---

## História 6: Avaliar Pedido 
**Como** um agente (empresa ou banco),  
**quero** analisar os pedidos de aluguel do ponto de vista financeiro,  
**para** emitir um parecer (positivo ou negativo) sobre a solicitação.  

**Testes de Aceitação:** 1. O agente deve ter acesso de leitura aos dados de renda e histórico de entidades empregadoras do cliente.  
2. Em caso de parecer positivo, o pedido deve ser libertado para a consideração de execução do contrato.

---

## História 7: Modificar Pedido 
**Como** um agente (empresa ou banco),  
**quero** modificar os detalhes de um pedido de aluguel,  
**para** adequar as condições do negócio durante a avaliação financeira.  

**Testes de Aceitação:** 1. O sistema deve registar no histórico do pedido que a modificação foi realizada por um agente.  
2. As alterações devem ficar visíveis para a consulta do cliente.

---

## História 8: Conceder Contrato de Crédito
**Como** um banco (agente),  
**quero** associar um contrato de crédito a um pedido de aluguel,  
**para** viabilizar o financiamento da locação para o cliente.  

**Testes de Aceitação:** 1. A ação de conceder crédito deve ser exclusiva para utilizadores autenticados com o perfil de "Banco".  
2. O crédito só pode ser associado a pedidos que já possuam um parecer financeiro positivo.

---

## História 9: Executar Contrato e Propriedade do Automóvel
**Como** um agente,  
**quero** executar o contrato de aluguel vinculando os dados do automóvel (matrícula, ano, marca, modelo e placa),  
**para** formalizar a locação e definir a propriedade do veículo.  

**Testes de Aceitação:** 1. O sistema deve exigir o preenchimento de matrícula, ano, marca, modelo e placa do veículo no momento da execução.  
2. O sistema deve permitir registar a propriedade do automóvel como sendo do cliente, da empresa ou do banco, dependendo do tipo de contrato executado.

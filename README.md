# Arquivo Oculto

Jogo de investigação criminal cooperativa para Android, desenvolvido em Godot 4.

## Conceito
Investigue casos por meio de documentos, fotografias, evidências, depoimentos e conexões entre suspeitos. A proposta é uma experiência de suspense e dedução, não um jogo de exploração 3D.

## Caso inicial
**Caso 001 — O desaparecimento de Blackwood**

O pacote estruturado do caso está em `content/case_001.json`. Ele reúne o resumo, linha do tempo, documentos, evidências, suspeitos e perguntas de investigação. A solução final permanece reservada para a etapa de conclusão, evitando revelar antecipadamente o mistério.

## Estado do desenvolvimento
- [x] Estrutura inicial do projeto Godot
- [x] Interface inicial e navegação do caso
- [x] Protótipo local de evidências, suspeitos e anotações
- [x] Primeira versão funcional do mural de conexões
- [x] Conteúdo-base estruturado do Caso 001 (JSON)
- [ ] Integrar documentos e depoimentos à interface do jogo
- [ ] Arte final para fotografias, documentos e retratos
- [ ] Persistência local do progresso
- [ ] Salas privadas e multiplayer online
- [ ] Exportação e testes em Android

## Próximas etapas
1. Integrar o pacote JSON às telas de documentos e depoimentos.
2. Implementar salvamento local do caso e das anotações.
3. Produzir os assets visuais do Caso 001.
4. Validar a compilação Godot e preparar a exportação Android.
5. Projetar e implementar o backend multiplayer e a sincronização das salas.

O multiplayer exige backend online; ele será implementado em etapa própria para permitir salas privadas e sincronização das pistas entre jogadores.

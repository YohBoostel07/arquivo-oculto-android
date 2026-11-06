# Arquivo Oculto — plano de desenvolvimento

## Decisões confirmadas
- Plataforma principal: Android.
- Estilo: investigação 2D por telas, salas, documentos, fotografias, interrogatórios e compartilhamento de pistas.
- Partidas: 2 a 4 jogadores.
- Modos desejados:
  - **Cooperativo em tempo real:** todos participam da mesma investigação e acompanham as descobertas compartilhadas.
  - **Investigações individuais:** cada participante investiga por conta própria e pode compartilhar suas descobertas com o grupo.
- Motor: Godot 4.
- Repositório: `YohBoostel07/arquivo-oculto-android`.

## Etapas

### Etapa 1 — Base jogável e progresso local
- [x] Estrutura inicial do projeto Godot.
- [x] Caso 001 — O Desaparecimento de Blackwood.
- [x] Telas de dossiê, evidências, suspeitos, documentos, depoimentos, mural e anotações.
- [x] Salvar evidências registradas, conexões e anotações no dispositivo.
- [ ] Validar o projeto no Godot e gerar o primeiro APK verificável.

### Etapa 2 — Identidade visual e experiência
- [ ] Criar identidade visual 2D definitiva.
- [ ] Adicionar retratos, fotografias, documentos e artes próprias do caso.
- [ ] Melhorar navegação, leitura e adaptação a diferentes telas Android.

### Etapa 3 — Multiplayer online
- [ ] Criar salas privadas para 2 a 4 jogadores.
- [ ] Criar entrada por código de sala.
- [ ] Implementar perfis e identificação dos participantes.
- [ ] Sincronizar pistas, anotações compartilhadas e estado da investigação.
- [ ] Separar progresso privado do progresso compartilhado.

### Etapa 4 — Modos de jogo
- [ ] Modo cooperativo em tempo real.
- [ ] Modo de investigação individual com compartilhamento opcional.
- [ ] Convites, reconexão e tratamento de desconexões.

### Etapa 5 — Conteúdo e lançamento
- [ ] Concluir e testar a solução do Caso 001.
- [ ] Adicionar novos casos com documentos, pistas e suspeitos.
- [ ] Testar partidas reais em diferentes aparelhos Android.
- [ ] Preparar APK/AAB de distribuição.

## Regra de validação
Uma etapa só será marcada como concluída depois de o código correspondente existir no repositório e, quando aplicável, passar por compilação e teste. A interface de salas não será apresentada como multiplayer online funcional antes de existir um servidor e a sincronização real entre aparelhos.

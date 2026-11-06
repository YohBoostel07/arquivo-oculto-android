extends Control

const BG = Color("#0b1220")
const PANEL = Color("#141f30")
const PANEL_ALT = Color("#1b2a3e")
const GOLD = Color("#d6ad62")
const MUTED = Color("#9aabc0")
const WHITE = Color("#edf2f7")

const EVIDENCE = [
	{"name": "Fotografia da sala", "detail": "O relógio de parede está parado às 22h17. A xícara sobre a mesa parece intacta.", "analysis": "A fotografia registra o relógio às 22h17, mas ainda não prova que esse foi o horário do desaparecimento."},
	{"name": "Bilhete rasgado", "detail": "Restam as palavras: “...não confie nele depois da meia-noite.” A assinatura foi arrancada.", "analysis": "O pronome “nele” não identifica ninguém. A referência à meia-noite pode ser literal ou uma forma de falar."},
	{"name": "Pegadas no corredor", "detail": "Marcas de lama seguem até o escritório, mas não há pegadas correspondentes do lado de fora da janela.", "analysis": "As marcas parecem seguir para o escritório. A janela aberta pode ter sido encenação, já que não há trilha externa correspondente."},
	{"name": "Registro telefônico", "detail": "Uma chamada de 4 minutos foi realizada às 21h53 para um número não identificado.", "analysis": "A duração indica que a chamada foi completada. Compare esse horário com o relato de Clara."}
]

const SUSPECTS = [
	{"name": "Arthur Blackwood — irmão", "detail": "Afirma que saiu da propriedade às 21h30. Diz não ter visto Helena naquela noite."},
	{"name": "Marta Vale — governanta", "detail": "Diz ter ouvido uma discussão no escritório pouco antes das 22h."},
	{"name": "Dr. Elias Moreau — médico", "detail": "Confirma uma visita naquela tarde, mas nega ter retornado à noite."},
	{"name": "Tomás Reed — vizinho", "detail": "Relata ter visto uma luz acesa no andar superior depois das 23h."},
	{"name": "Clara Nunes — amiga", "detail": "Recebeu uma ligação de Helena às 21h53, mas afirma que a chamada caiu."}
]

var content: VBoxContainer
var selected_evidence := "Fotografia da sala"
var discovered: Array[String] = []
var notes := ""
var board_selection: Array[String] = []
var connections: Array[String] = []

func _ready() -> void:
	_build_home()

func _clear() -> void:
	for child in get_children():
		child.queue_free()

func _style_panel(panel: PanelContainer, color: Color = PANEL) -> void:
	var box := StyleBoxFlat.new()
	box.bg_color = color
	box.border_color = Color("#304158")
	box.set_border_width_all(1)
	box.set_corner_radius_all(10)
	box.content_margin_left = 18
	box.content_margin_right = 18
	box.content_margin_top = 14
	box.content_margin_bottom = 14
	panel.add_theme_stylebox_override("panel", box)

func _label(text_value: String, size: int = 18, color: Color = WHITE) -> Label:
	var label := Label.new()
	label.text = text_value
	label.add_theme_font_size_override("font_size", size)
	label.add_theme_color_override("font_color", color)
	label.autowrap_mode = TextServer.AUTOWRAP_WORD_SMART
	return label

func _button(text_value: String, callback: Callable) -> Button:
	var button := Button.new()
	button.text = text_value
	button.custom_minimum_size = Vector2(0, 48)
	button.size_flags_horizontal = Control.SIZE_EXPAND_FILL
	button.add_theme_font_size_override("font_size", 16)
	button.add_theme_color_override("font_color", WHITE)
	var normal := StyleBoxFlat.new()
	normal.bg_color = PANEL_ALT
	normal.set_corner_radius_all(8)
	normal.content_margin_left = 12
	normal.content_margin_right = 12
	button.add_theme_stylebox_override("normal", normal)
	var hover := normal.duplicate()
	hover.bg_color = Color("#293d56")
	button.add_theme_stylebox_override("hover", hover)
	button.pressed.connect(callback)
	return button

func _base() -> void:
	_clear()
	var background := ColorRect.new()
	background.color = BG
	background.set_anchors_and_offsets_preset(Control.PRESET_FULL_RECT)
	add_child(background)

	var scroll := ScrollContainer.new()
	scroll.set_anchors_and_offsets_preset(Control.PRESET_FULL_RECT)
	scroll.horizontal_scroll_mode = ScrollContainer.SCROLL_MODE_DISABLED
	add_child(scroll)

	var margin := MarginContainer.new()
	margin.size_flags_horizontal = Control.SIZE_EXPAND_FILL
	margin.add_theme_constant_override("margin_left", 20)
	margin.add_theme_constant_override("margin_right", 20)
	margin.add_theme_constant_override("margin_top", 20)
	margin.add_theme_constant_override("margin_bottom", 24)
	scroll.add_child(margin)

	content = VBoxContainer.new()
	content.size_flags_horizontal = Control.SIZE_EXPAND_FILL
	content.add_theme_constant_override("separation", 14)
	margin.add_child(content)

func _build_home() -> void:
	_base()
	content.add_child(_label("ARQUIVO  /  001", 15, GOLD))
	content.add_child(_label("ARQUIVO OCULTO", 38, WHITE))
	content.add_child(_label("Toda pista conta. Toda versão pode esconder uma mentira.", 17, MUTED))
	var spacer := Control.new()
	spacer.custom_minimum_size.y = 34
	content.add_child(spacer)

	var card := PanelContainer.new()
	_style_panel(card)
	content.add_child(card)
	var inner := VBoxContainer.new()
	inner.add_theme_constant_override("separation", 12)
	card.add_child(inner)
	inner.add_child(_label("CASO 001  •  O DESAPARECIMENTO DE BLACKWOOD", 19, GOLD))
	inner.add_child(_label("Uma casa abandonada. Cinco suspeitos. Uma pessoa desaparecida. Examine documentos, compare depoimentos e descubra o que aconteceu.", 16))
	inner.add_child(_button("INICIAR INVESTIGAÇÃO", _show_dossier))
	content.add_child(_label("PROTÓTIPO  •  INVESTIGAÇÃO LOCAL", 13, MUTED))

func _case_shell() -> void:
	_base()
	var top := HBoxContainer.new()
	content.add_child(top)
	var heading := VBoxContainer.new()
	heading.size_flags_horizontal = Control.SIZE_EXPAND_FILL
	top.add_child(heading)
	heading.add_child(_label("CASO 001", 14, GOLD))
	heading.add_child(_label("O desaparecimento de Blackwood", 22))
	top.add_child(_button("← MENU", _build_home))

	var tabs := GridContainer.new()
	tabs.columns = 2
	tabs.add_theme_constant_override("h_separation", 8)
	tabs.add_theme_constant_override("v_separation", 8)
	content.add_child(tabs)
	tabs.add_child(_button("Dossiê", _show_dossier))
	tabs.add_child(_button("Evidências", _show_evidence))
	tabs.add_child(_button("Suspeitos", _show_suspects))
	tabs.add_child(_button("Mural de pistas", _show_board))
	tabs.add_child(_button("Anotações", _show_notes))
	content.add_child(HSeparator.new())

func _section(title: String, description: String) -> void:
	content.add_child(_label(title, 24, GOLD))
	content.add_child(_label(description, 16, MUTED))

func _show_dossier() -> void:
	_case_shell()
	_section("O que sabemos", "Na noite de 17 de outubro, Helena Blackwood desapareceu de sua residência. A porta da frente estava trancada por dentro. Não havia sinais claros de arrombamento.")
	var panel := PanelContainer.new()
	_style_panel(panel)
	content.add_child(panel)
	var body := VBoxContainer.new()
	body.add_theme_constant_override("separation", 10)
	panel.add_child(body)
	body.add_child(_label("RELATÓRIO INICIAL", 15, GOLD))
	body.add_child(_label("A polícia encontrou uma xícara ainda morna sobre a mesa, uma janela aberta no escritório e marcas de lama no corredor. O relógio da sala parou às 22h17.", 16))
	body.add_child(_label("PONTO A ESCLARECER", 14, GOLD))
	body.add_child(_label("O horário do relógio coincide com o momento real do desaparecimento ou foi alterado?", 15))
	content.add_child(_label("Progresso: %d de %d evidências registradas" % [discovered.size(), EVIDENCE.size()], 15, GOLD))
	content.add_child(_button("EXAMINAR EVIDÊNCIAS", _show_evidence))
	content.add_child(_button("ABRIR MURAL DE CONEXÕES", _show_board))

func _show_evidence() -> void:
	_case_shell()
	_section("Evidências", "Abra cada item, leia a análise e registre as pistas que considerar relevantes.")
	for item in EVIDENCE:
		var panel := PanelContainer.new()
		_style_panel(panel)
		content.add_child(panel)
		var body := VBoxContainer.new()
		body.add_theme_constant_override("separation", 8)
		panel.add_child(body)
		var status := "  ✓ REGISTRADA" if discovered.has(item["name"]) else ""
		body.add_child(_label(item["name"] + status, 17, GOLD))
		body.add_child(_label(item["detail"], 15))
		body.add_child(_button("EXAMINAR / REGISTRAR", _evidence_detail.bind(item)))

func _evidence_detail(item: Dictionary) -> void:
	selected_evidence = item["name"]
	var dialog := AcceptDialog.new()
	dialog.title = item["name"]
	dialog.dialog_text = item["detail"] + "\n\nANÁLISE\n" + item["analysis"] + "\n\nDeseja registrar esta evidência no seu quadro?"
	dialog.ok_button_text = "Registrar pista"
	add_child(dialog)
	dialog.confirmed.connect(func() -> void:
		if not discovered.has(item["name"]):
			discovered.append(item["name"])
		_show_evidence()
	)
	dialog.canceled.connect(func() -> void: dialog.queue_free())
	dialog.confirmed.connect(func() -> void: dialog.queue_free())
	dialog.popup_centered(Vector2i(520, 300))

func _show_suspects() -> void:
	_case_shell()
	_section("Pessoas de interesse", "Compare os relatos. Uma divergência é um ponto de partida, não uma prova de culpa.")
	for suspect in SUSPECTS:
		var panel := PanelContainer.new()
		_style_panel(panel)
		content.add_child(panel)
		var body := VBoxContainer.new()
		body.add_theme_constant_override("separation", 8)
		panel.add_child(body)
		body.add_child(_label(suspect["name"], 17, GOLD))
		body.add_child(_label(suspect["detail"], 15))

func _show_board() -> void:
	_case_shell()
	_section("Mural de conexões", "Selecione duas evidências ou pessoas para registrar uma ligação investigativa. As conexões ficam salvas durante esta sessão.")
	content.add_child(_label("EVIDÊNCIAS REGISTRADAS", 15, GOLD))
	if discovered.is_empty():
		content.add_child(_label("Nenhuma pista registrada. Vá até Evidências e registre pelo menos dois itens.", 15, MUTED))
	else:
		for evidence_name in discovered:
			content.add_child(_button("＋  " + evidence_name, _toggle_board_item.bind(evidence_name)))
	content.add_child(_label("SUSPEITOS", 15, GOLD))
	for suspect in SUSPECTS:
		content.add_child(_button("＋  " + suspect["name"], _toggle_board_item.bind(suspect["name"])))
	content.add_child(_label("Selecionados: " + ("  ↔  ".join(board_selection) if not board_selection.is_empty() else "nenhum"), 14, MUTED))
	content.add_child(_button("CRIAR CONEXÃO ENTRE SELECIONADOS", _create_connection))
	content.add_child(_label("CONEXÕES REGISTRADAS", 15, GOLD))
	if connections.is_empty():
		content.add_child(_label("Ainda não há conexões. Selecione dois itens e crie uma ligação.", 15, MUTED))
	else:
		for connection in connections:
			var row := HBoxContainer.new()
			content.add_child(row)
			row.add_child(_label("• " + connection, 14, WHITE))
			row.add_child(_button("REMOVER", _remove_connection.bind(connection)))

func _toggle_board_item(item_name: String) -> void:
	if board_selection.has(item_name):
		board_selection.erase(item_name)
	elif board_selection.size() < 2:
		board_selection.append(item_name)
	else:
		board_selection = [item_name]
	_show_board()

func _create_connection() -> void:
	if board_selection.size() != 2:
		_show_board()
		return
	var connection := board_selection[0] + "  ↔  " + board_selection[1]
	if not connections.has(connection):
		connections.append(connection)
	board_selection.clear()
	_show_board()

func _remove_connection(connection: String) -> void:
	connections.erase(connection)
	_show_board()

func _show_notes() -> void:
	_case_shell()
	_section("Caderno do investigador", "Anote teorias, horários e perguntas. O conteúdo permanece enquanto o jogo estiver aberto.")
	var edit := TextEdit.new()
	edit.custom_minimum_size = Vector2(0, 190)
	edit.size_flags_horizontal = Control.SIZE_EXPAND_FILL
	edit.placeholder_text = "Registre conexões, horários e dúvidas..."
	edit.text = notes
	content.add_child(edit)
	content.add_child(_button("SALVAR ANOTAÇÕES", _save_notes.bind(edit)))
	content.add_child(_label("Pistas registradas: " + str(discovered.size()) + "  •  Conexões: " + str(connections.size()), 14, GOLD))

func _save_notes(edit: TextEdit) -> void:
	notes = edit.text
	_show_notes()

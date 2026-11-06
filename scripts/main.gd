extends Control

const BG = Color("#0b1220")
const PANEL = Color("#141f30")
const PANEL_ALT = Color("#1b2a3e")
const GOLD = Color("#d6ad62")
const MUTED = Color("#9aabc0")
const WHITE = Color("#edf2f7")

var content: VBoxContainer
var selected_evidence := "Fotografia da sala"
var discovered := []
var notes := ""

func _ready():
	_build_home()

func _clear():
	for child in get_children():
		child.queue_free()

func _style_panel(panel: PanelContainer, color: Color = PANEL):
	var box = StyleBoxFlat.new()
	box.bg_color = color
	box.border_color = Color("#304158")
	box.set_border_width_all(1)
	box.set_corner_radius_all(10)
	box.content_margin_left = 18
	box.content_margin_right = 18
	box.content_margin_top = 14
	box.content_margin_bottom = 14
	panel.add_theme_stylebox_override("panel", box)

func _label(text: String, size: int = 18, color: Color = WHITE) -> Label:
	var l = Label.new()
	l.text = text
	l.add_theme_font_size_override("font_size", size)
	l.add_theme_color_override("font_color", color)
	l.autowrap_mode = TextServer.AUTOWRAP_WORD_SMART
	return l

func _button(text: String, callback: Callable) -> Button:
	var b = Button.new()
	b.text = text
	b.custom_minimum_size = Vector2(0, 48)
	b.add_theme_font_size_override("font_size", 17)
	b.add_theme_color_override("font_color", WHITE)
	var normal = StyleBoxFlat.new()
	normal.bg_color = PANEL_ALT
	normal.set_corner_radius_all(8)
	normal.content_margin_left = 14
	normal.content_margin_right = 14
	b.add_theme_stylebox_override("normal", normal)
	var hover = normal.duplicate()
	hover.bg_color = Color("#293d56")
	b.add_theme_stylebox_override("hover", hover)
	b.pressed.connect(callback)
	return b

func _base():
	_clear()
	var bg = ColorRect.new()
	bg.color = BG
	bg.set_anchors_and_offsets_preset(Control.PRESET_FULL_RECT)
	add_child(bg)
	var margin = MarginContainer.new()
	margin.set_anchors_and_offsets_preset(Control.PRESET_FULL_RECT)
	margin.add_theme_constant_override("margin_left", 34)
	margin.add_theme_constant_override("margin_right", 34)
	margin.add_theme_constant_override("margin_top", 24)
	margin.add_theme_constant_override("margin_bottom", 24)
	add_child(margin)
	content = VBoxContainer.new()
	content.add_theme_constant_override("separation", 16)
	margin.add_child(content)

func _build_home():
	_base()
	content.add_child(_label("ARQUIVO  /  001", 15, GOLD))
	content.add_child(_label("ARQUIVO OCULTO", 42, WHITE))
	content.add_child(_label("Toda pista conta. Toda versão pode esconder uma mentira.", 19, MUTED))
	var spacer = Control.new()
	spacer.size_flags_vertical = Control.SIZE_EXPAND_FILL
	content.add_child(spacer)
	var card = PanelContainer.new()
	_style_panel(card)
	content.add_child(card)
	var inner = VBoxContainer.new()
	inner.add_theme_constant_override("separation", 12)
	card.add_child(inner)
	inner.add_child(_label("CASO 001  •  O DESAPARECIMENTO DE BLACKWOOD", 20, GOLD))
	inner.add_child(_label("Uma casa abandonada. Cinco suspeitos. Uma pessoa desaparecida.\nExamine documentos, compare depoimentos e descubra o que aconteceu.", 17))
	inner.add_child(_button("INICIAR INVESTIGAÇÃO", _build_case))
	content.add_child(_label("VERSÃO DE DESENVOLVIMENTO  •  MODO LOCAL", 13, MUTED))

func _build_case():
	_base()
	var top = HBoxContainer.new()
	content.add_child(top)
	var title = VBoxContainer.new()
	title.size_flags_horizontal = Control.SIZE_EXPAND_FILL
	top.add_child(title)
	title.add_child(_label("CASO 001", 14, GOLD))
	title.add_child(_label("O desaparecimento de Blackwood", 27))
	top.add_child(_button("← MENU", _build_home))
	var tabs = GridContainer.new()
	tabs.columns = 4
	content.add_child(tabs)
	tabs.add_child(_button("Dossiê", _show_dossier))
	tabs.add_child(_button("Evidências", _show_evidence))
	tabs.add_child(_button("Suspeitos", _show_suspects))
	tabs.add_child(_button("Anotações", _show_notes))
	var divider = HSeparator.new()
	content.add_child(divider)
	_show_dossier()

func _section(title: String, description: String):
	content.add_child(_label(title, 25, GOLD))
	content.add_child(_label(description, 17, MUTED))

func _show_dossier():
	_build_case_shell()
	_section("O que sabemos", "Na noite de 17 de outubro, Helena Blackwood desapareceu de sua residência. A porta da frente estava trancada por dentro. Não havia sinais claros de arrombamento.")
	var p = PanelContainer.new()
	_style_panel(p)
	content.add_child(p)
	var v = VBoxContainer.new()
	p.add_child(v)
	v.add_child(_label("RELATÓRIO INICIAL", 16, GOLD))
	v.add_child(_label("A polícia encontrou uma xícara ainda morna sobre a mesa, uma janela aberta no escritório e marcas de lama no corredor. O relógio da sala parou às 22h17.", 18))
	content.add_child(_label("Objetivo: descubra qual depoimento contradiz as evidências físicas.", 16, WHITE))

func _build_case_shell():
	_build_case()

func _show_evidence():
	_build_case_shell()
	_section("Evidências coletadas", "Selecione um item para examinar seus detalhes. Registre o que considerar importante.")
	var items = [
		["Fotografia da sala", "O relógio de parede está parado às 22h17. A xícara sobre a mesa parece intacta."],
		["Bilhete rasgado", "Restam as palavras: “...não confie nele depois da meia-noite.” A assinatura foi arrancada."],
		["Pegadas no corredor", "Marcas de lama seguem até o escritório, mas não há pegadas correspondentes do lado de fora da janela."],
		["Registro telefônico", "Uma chamada de 4 minutos foi realizada às 21h53 para um número não identificado."]
	]
	for item in items:
		var b = _button(item[0], func(): _evidence_detail(item[0], item[1]))
		content.add_child(b)
	if selected_evidence != "":
		content.add_child(_label("EM EXAME: " + selected_evidence, 16, GOLD))

func _evidence_detail(name: String, detail: String):
	selected_evidence = name
	var dialog = AcceptDialog.new()
	dialog.title = name
	dialog.dialog_text = detail + "\n\nAdicione esta pista às suas descobertas?"
	dialog.ok_button_text = "Registrar pista"
	add_child(dialog)
	dialog.confirmed.connect(func():
		if not discovered.has(name):
			discovered.append(name)
	)
	dialog.popup_centered(Vector2i(480, 220))

func _show_suspects():
	_build_case_shell()
	_section("Pessoas de interesse", "Compare os relatos. Nem toda contradição significa culpa.")
	var suspects = [
		["Arthur Blackwood — irmão", "Afirma que saiu da propriedade às 21h30. Diz não ter visto Helena naquela noite."],
		["Marta Vale — governanta", "Diz ter ouvido uma discussão no escritório pouco antes das 22h."],
		["Dr. Elias Moreau — médico", "Confirma uma visita naquela tarde, mas nega ter retornado à noite."],
		["Tomás Reed — vizinho", "Relata ter visto uma luz acesa no andar superior depois das 23h."],
		["Clara Nunes — amiga", "Recebeu uma ligação de Helena às 21h53, mas afirma que a chamada caiu."]
	]
	for s in suspects:
		var panel = PanelContainer.new()
		_style_panel(panel)
		content.add_child(panel)
		var v = VBoxContainer.new()
		panel.add_child(v)
		v.add_child(_label(s[0], 18, GOLD))
		v.add_child(_label(s[1], 16, WHITE))

func _show_notes():
	_build_case_shell()
	_section("Caderno do investigador", "Suas anotações ficam disponíveis nesta sessão.")
	var edit = TextEdit.new()
	edit.custom_minimum_size = Vector2(0, 180)
	edit.placeholder_text = "Registre conexões, horários e dúvidas..."
	edit.text = notes
	content.add_child(edit)
	content.add_child(_button("SALVAR ANOTAÇÕES", func(): notes = edit.text; _show_notes()))
	content.add_child(_label("Pistas registradas: " + str(discovered.size()), 15, GOLD))

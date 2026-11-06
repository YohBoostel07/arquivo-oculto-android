extends Control

const BG := Color("#090f18")
const PANEL := Color("#141f2d")
const PANEL_LIGHT := Color("#1b2a3b")
const GOLD := Color("#d6ad68")
const MUTED := Color("#9aa9b9")
const WHITE := Color("#f1f3f5")

var content: VBoxContainer

func _ready() -> void:
	_build_home()

func _build_home() -> void:
	for child in get_children():
		child.queue_free()
	var background := ColorRect.new()
	background.color = BG
	background.set_anchors_and_offsets_preset(Control.PRESET_FULL_RECT)
	add_child(background)

	var margin := MarginContainer.new()
	margin.set_anchors_and_offsets_preset(Control.PRESET_FULL_RECT)
	margin.add_theme_constant_override("margin_left", 38)
	margin.add_theme_constant_override("margin_right", 38)
	margin.add_theme_constant_override("margin_top", 54)
	margin.add_theme_constant_override("margin_bottom", 42)
	add_child(margin)

	content = VBoxContainer.new()
	content.add_theme_constant_override("separation", 22)
	margin.add_child(content)

	var top := HBoxContainer.new()
	content.add_child(top)
	var mark := Label.new()
	mark.text = "AO"
	mark.add_theme_font_size_override("font_size", 22)
	mark.add_theme_color_override("font_color", GOLD)
	top.add_child(mark)
	var brand := Label.new()
	brand.text = "  ARQUIVO OCULTO"
	brand.add_theme_font_size_override("font_size", 17)
	brand.add_theme_color_override("font_color", WHITE)
	top.add_child(brand)

	var spacer := Control.new()
	spacer.custom_minimum_size.y = 90
	content.add_child(spacer)

	var eyebrow := Label.new()
	eyebrow.text = "DOSSIÊ 001  /  ACESSO RESTRITO"
	eyebrow.add_theme_font_size_override("font_size", 12)
	eyebrow.add_theme_color_override("font_color", GOLD)
	content.add_child(eyebrow)

	var title := Label.new()
	title.text = "Toda verdade\ndeixa rastros."
	title.add_theme_font_size_override("font_size", 43)
	title.add_theme_color_override("font_color", WHITE)
	title.autowrap_mode = TextServer.AUTOWRAP_WORD_SMART
	content.add_child(title)

	var intro := Label.new()
	intro.text = "Investigue casos inexplicáveis. Reúna evidências. Descubra o que realmente aconteceu."
	intro.add_theme_font_size_override("font_size", 17)
	intro.add_theme_color_override("font_color", MUTED)
	intro.autowrap_mode = TextServer.AUTOWRAP_WORD_SMART
	content.add_child(intro)

	var gap := Control.new()
	gap.custom_minimum_size.y = 22
	content.add_child(gap)

	var case_card := PanelContainer.new()
	case_card.add_theme_stylebox_override("panel", _panel_style(PANEL_LIGHT, GOLD))
	content.add_child(case_card)
	var case_box := VBoxContainer.new()
	case_box.add_theme_constant_override("separation", 12)
	case_card.add_child(case_box)
	var case_tag := Label.new()
	case_tag.text = "PRIMEIRO CASO  •  INVESTIGAÇÃO DISPONÍVEL"
	case_tag.add_theme_font_size_override("font_size", 11)
	case_tag.add_theme_color_override("font_color", GOLD)
	case_box.add_child(case_tag)
	var case_title := Label.new()
	case_title.text = "O Desaparecimento\nde Blackwood"
	case_title.add_theme_font_size_override("font_size", 25)
	case_title.add_theme_color_override("font_color", WHITE)
	case_box.add_child(case_title)
	var case_desc := Label.new()
	case_desc.text = "Uma cidade pequena. Uma noite sem testemunhas. E uma pessoa que parece nunca ter existido."
	case_desc.add_theme_font_size_override("font_size", 14)
	case_desc.add_theme_color_override("font_color", MUTED)
	case_desc.autowrap_mode = TextServer.AUTOWRAP_WORD_SMART
	case_box.add_child(case_desc)
	var start := Button.new()
	start.text = "ABRIR DOSSIÊ   →"
	start.custom_minimum_size.y = 58
	start.add_theme_font_size_override("font_size", 16)
	start.add_theme_color_override("font_color", BG)
	start.add_theme_stylebox_override("normal", _panel_style(GOLD, GOLD))
	start.add_theme_stylebox_override("hover", _panel_style(Color("#e6c58d"), Color("#e6c58d")))
	start.pressed.connect(_open_case)
	case_box.add_child(start)

	var section_gap := Control.new()
	section_gap.custom_minimum_size.y = 10
	content.add_child(section_gap)
	var mode_label := Label.new()
	mode_label.text = "SUA CENTRAL DE INVESTIGAÇÃO"
	mode_label.add_theme_font_size_override("font_size", 12)
	mode_label.add_theme_color_override("font_color", MUTED)
	content.add_child(mode_label)
	var modes := HBoxContainer.new()
	modes.add_theme_constant_override("separation", 12)
	content.add_child(modes)
	_add_mode(modes, "01", "Investigar", "Casos e pistas")
	_add_mode(modes, "02", "Equipe", "2 a 4 jogadores")

	var bottom_space := Control.new()
	bottom_space.size_flags_vertical = Control.SIZE_EXPAND_FILL
	content.add_child(bottom_space)
	var footer := Label.new()
	footer.text = "ARQUIVO OCULTO  •  PROTÓTIPO INICIAL"
	footer.horizontal_alignment = HORIZONTAL_ALIGNMENT_CENTER
	footer.add_theme_font_size_override("font_size", 10)
	footer.add_theme_color_override("font_color", Color("#647386"))
	content.add_child(footer)

func _add_mode(parent: HBoxContainer, number: String, heading: String, sub: String) -> void:
	var panel := PanelContainer.new()
	panel.size_flags_horizontal = Control.SIZE_EXPAND_FILL
	panel.add_theme_stylebox_override("panel", _panel_style(PANEL, Color("#26374a")))
	parent.add_child(panel)
	var box := VBoxContainer.new()
	box.add_theme_constant_override("separation", 7)
	panel.add_child(box)
	var n := Label.new()
	n.text = number
	n.add_theme_color_override("font_color", GOLD)
	n.add_theme_font_size_override("font_size", 12)
	box.add_child(n)
	var h := Label.new()
	h.text = heading
	h.add_theme_color_override("font_color", WHITE)
	h.add_theme_font_size_override("font_size", 16)
	box.add_child(h)
	var s := Label.new()
	s.text = sub
	s.add_theme_color_override("font_color", MUTED)
	s.add_theme_font_size_override("font_size", 11)
	box.add_child(s)

func _panel_style(fill: Color, border: Color) -> StyleBoxFlat:
	var style := StyleBoxFlat.new()
	style.bg_color = fill
	style.border_color = border
	style.set_border_width_all(1)
	style.set_corner_radius_all(12)
	style.content_margin_left = 18
	style.content_margin_right = 18
	style.content_margin_top = 18
	style.content_margin_bottom = 18
	return style

func _open_case() -> void:
	for child in content.get_children():
		child.queue_free()
	var title := Label.new()
	title.text = "DOSSIÊ 001"
	title.add_theme_font_size_override("font_size", 14)
	title.add_theme_color_override("font_color", GOLD)
	content.add_child(title)
	var heading := Label.new()
	heading.text = "O Desaparecimento de Blackwood"
	heading.add_theme_font_size_override("font_size", 30)
	heading.add_theme_color_override("font_color", WHITE)
	heading.autowrap_mode = TextServer.AUTOWRAP_WORD_SMART
	content.add_child(heading)
	var body := Label.new()
	body.text = "BLACKWOOD, 02:17\n\nA polícia encontrou a casa aberta. O jantar ainda estava sobre a mesa. Não havia sinais de arrombamento, luta ou fuga. Elias Blackwood desapareceu sem deixar qualquer explicação.\n\nSua investigação começa aqui. Em breve: cena do crime, documentos, depoimentos e um painel para conectar evidências."
	body.add_theme_font_size_override("font_size", 17)
	body.add_theme_color_override("font_color", MUTED)
	body.autowrap_mode = TextServer.AUTOWRAP_WORD_SMART
	content.add_child(body)
	var back := Button.new()
	back.text = "VOLTAR AO INÍCIO"
	back.custom_minimum_size.y = 54
	back.pressed.connect(_build_home)
	content.add_child(back)

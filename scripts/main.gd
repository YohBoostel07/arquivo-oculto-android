extends Control

const BG := Color("#090f18")
const PANEL := Color("#172333")
const GOLD := Color("#d6ad68")
const MUTED := Color("#aab5c2")
const WHITE := Color("#f3f4f6")

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
	margin.add_theme_constant_override("margin_left", 34)
	margin.add_theme_constant_override("margin_right", 34)
	margin.add_theme_constant_override("margin_top", 54)
	margin.add_theme_constant_override("margin_bottom", 40)
	add_child(margin)

	content = VBoxContainer.new()
	content.add_theme_constant_override("separation", 22)
	margin.add_child(content)

	var brand := Label.new()
	brand.text = "AO   /   ARQUIVO OCULTO"
	brand.add_theme_font_size_override("font_size", 16)
	brand.add_theme_color_override("font_color", GOLD)
	content.add_child(brand)

	var spacer := Control.new()
	spacer.custom_minimum_size.y = 95
	content.add_child(spacer)

	var eyebrow := Label.new()
	eyebrow.text = "DOSSIÊ 001  •  ACESSO RESTRITO"
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

	var card := PanelContainer.new()
	card.add_theme_stylebox_override("panel", _panel_style(PANEL, GOLD))
	content.add_child(card)

	var card_box := VBoxContainer.new()
	card_box.add_theme_constant_override("separation", 13)
	card.add_child(card_box)

	var tag := Label.new()
	tag.text = "PRIMEIRO CASO  /  INVESTIGAÇÃO"
	tag.add_theme_font_size_override("font_size", 11)
	tag.add_theme_color_override("font_color", GOLD)
	card_box.add_child(tag)

	var case_title := Label.new()
	case_title.text = "O Desaparecimento\nde Blackwood"
	case_title.add_theme_font_size_override("font_size", 26)
	case_title.add_theme_color_override("font_color", WHITE)
	card_box.add_child(case_title)

	var description := Label.new()
	description.text = "Uma cidade pequena. Uma noite sem testemunhas. Uma pessoa que parece nunca ter existido."
	description.add_theme_font_size_override("font_size", 14)
	description.add_theme_color_override("font_color", MUTED)
	description.autowrap_mode = TextServer.AUTOWRAP_WORD_SMART
	card_box.add_child(description)

	var start := Button.new()
	start.text = "ABRIR DOSSIÊ     →"
	start.custom_minimum_size.y = 60
	start.add_theme_font_size_override("font_size", 16)
	start.add_theme_color_override("font_color", BG)
	start.add_theme_stylebox_override("normal", _panel_style(GOLD, GOLD))
	start.add_theme_stylebox_override("hover", _panel_style(Color("#e7c68e"), Color("#e7c68e")))
	start.pressed.connect(_open_case)
	card_box.add_child(start)

	var footer_space := Control.new()
	footer_space.size_flags_vertical = Control.SIZE_EXPAND_FILL
	content.add_child(footer_space)

	var footer := Label.new()
	footer.text = "PROTÓTIPO INICIAL  •  VERSÃO 0.1"
	footer.horizontal_alignment = HORIZONTAL_ALIGNMENT_CENTER
	footer.add_theme_font_size_override("font_size", 10)
	footer.add_theme_color_override("font_color", Color("#68788b"))
	content.add_child(footer)

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

	var heading := Label.new()
	heading.text = "DOSSIÊ 001"
	heading.add_theme_font_size_override("font_size", 14)
	heading.add_theme_color_override("font_color", GOLD)
	content.add_child(heading)

	var title := Label.new()
	title.text = "O Desaparecimento de Blackwood"
	title.add_theme_font_size_override("font_size", 30)
	title.add_theme_color_override("font_color", WHITE)
	title.autowrap_mode = TextServer.AUTOWRAP_WORD_SMART
	content.add_child(title)

	var body := Label.new()
	body.text = "BLACKWOOD  /  02:17\n\nA polícia encontrou a casa aberta. O jantar ainda estava sobre a mesa. Não havia sinais de arrombamento, luta ou fuga. Elias Blackwood desapareceu sem deixar explicações.\n\nSua investigação começa aqui. Esta primeira versão confirma a instalação e a abertura do aplicativo. As mecânicas de investigação serão construídas nas próximas etapas."
	body.add_theme_font_size_override("font_size", 17)
	body.add_theme_color_override("font_color", MUTED)
	body.autowrap_mode = TextServer.AUTOWRAP_WORD_SMART
	content.add_child(body)

	var spacer := Control.new()
	spacer.size_flags_vertical = Control.SIZE_EXPAND_FILL
	content.add_child(spacer)

	var back := Button.new()
	back.text = "VOLTAR AO INÍCIO"
	back.custom_minimum_size.y = 56
	back.pressed.connect(_build_home)
	content.add_child(back)

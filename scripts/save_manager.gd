extends Node

const SAVE_PATH := "user://arquivo_oculto_save.json"

func load_investigation() -> Dictionary:
	if not FileAccess.file_exists(SAVE_PATH):
		return _default_data()

	var file := FileAccess.open(SAVE_PATH, FileAccess.READ)
	if file == null:
		push_warning("Não foi possível abrir o arquivo de progresso.")
		return _default_data()

	var parsed: Variant = JSON.parse_string(file.get_as_text())
	if typeof(parsed) != TYPE_DICTIONARY:
		push_warning("Arquivo de progresso inválido. Um novo progresso será iniciado.")
		return _default_data()

	var data: Dictionary = parsed
	return {
		"discovered": _string_array(data.get("discovered", [])),
		"notes": str(data.get("notes", "")),
		"connections": _string_array(data.get("connections", []))
	}

func save_investigation(discovered: Array, notes: String, connections: Array) -> bool:
	var data := {
		"discovered": discovered,
		"notes": notes,
		"connections": connections
	}

	var file := FileAccess.open(SAVE_PATH, FileAccess.WRITE)
	if file == null:
		push_error("Não foi possível salvar o progresso da investigação.")
		return false

	file.store_string(JSON.stringify(data, "\t"))
	return true

func _default_data() -> Dictionary:
	return {
		"discovered": [],
		"notes": "",
		"connections": []
	}

func _string_array(value: Variant) -> Array:
	var result: Array = []
	if typeof(value) == TYPE_ARRAY:
		for item in value:
			result.append(str(item))
	return result

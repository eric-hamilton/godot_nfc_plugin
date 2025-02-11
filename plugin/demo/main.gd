extends Node2D

var nfc_plugin = null
@onready var label: Label = $Label
@onready var query_button: Button = $QueryButton  # Ensure you have a Button node named 'QueryButton'

func _ready():
	# Get the NFCPlugin singleton from Engine
	nfc_plugin = Engine.get_singleton("NFCPlugin")

	if nfc_plugin:
		# Connect the NFC detected signal
		nfc_plugin.connect("nfc_tag_detected", Callable(self, "_on_nfc_detected"))

		# Check if NFC is available
		if nfc_plugin.isNfcAvailable():  # Ensure this method exists in your plugin
			label.text = "NFC plugin started"
		else:
			label.text = "NFC not available on this device"
	else:
		label.text = "NFC plugin not found"


func _on_nfc_detected(tag_id: String) -> void:
	# Update label with detected NFC tag id
	label.text = "NFC Tag: " + tag_id
	nfc_plugin.stopNFCReading() 

func _on_query_button_pressed() -> void:
	if nfc_plugin:
		label.text = "starting"
		nfc_plugin.startNFCReading()  # Ensure this method exists in your NFC plugin

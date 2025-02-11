# Godot NFC Plugin
This is a simple NFC plugin for Godot. It is forked from the [official Godot Android Plugin repo.](https://github.com/m4gr3d/Godot-Android-Plugin-Template)

## How it works
The plugin exposes the NFC reader and supplies several functions to be called from Godot

### startNFCReading
Enables ReaderMode in the NFCAdapter

### stopNFCReading
Disables ReaderMode in the NFCAdapter

### getState
Queries if the NFCAdapter is in reader mode

### isNfcAvailable
Queries if the NFCAdapter is available

### onTagDiscovered
Emits a Godot-Compatible signal with the tagId as a string argument

param (
	[string]$version = $(throw "specify version (example 0.3)"),
	[string]$source = "G:\dev\ps3mediaserver-read-only\ps3mediaserver\plugins"
)

$ErrorActionPreference = "stop"

if ($version.StartsWith("v")) {
	throw "Do not prefix version specifiers with 'v'."
}

$archive = "SoundCloud4PS3-$version.zip"
pushd $source
if (test-path $archive) {
	rm $archive
}
& "C:\Program Files\WinRAR\WinRAR.exe" a -as -afzip -ep1 -t $archive `
	"$source\apache-mime4j-0.6.jar" `
	"$source\commons-logging-1.1.1.jar" `
	"$source\httpclient-4.0.jar" `
	"$source\httpcore-4.0.1.jar" `
	"$source\httpmime-4.0.jar" `
	"$source\org.urbanstew.soundcloudapi-0.9.2.jar" `
	"$source\signpost-commonshttp4-1.1.jar" `
	"$source\signpost-core-1.1.jar" `
	"$source\SoundCloud4PS3-$version.jar"
popd
start-sleep 3
move -force "$source\$archive" .

# Create network

```bash
$ docker create network conduit_network
```

Then added to devcontainer.json

```json
	"runArgs": [
		"--name=conduit_backend",
		"--network=conduit_network",
		"--hostname=backend"
	]
```
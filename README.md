# ToF Pulling Simulator

ToF Pulling Simulator lets you simulate Tower of Fantasy's banner orders.

## Installing

```bash
$ git clone git@github.com:SCThijsse/tof-pull-simulator.git
$ mvn clean package
```

### Requirements

 - Maven 3.6
 - Java 17

## Modules

The project has several modules to use:

- `simulator-common` contains common logic for the simulator.
- `simulator-api` contains a rest API for running the simulator.
- `simulator-bot` contains a Discord Bot for running the simulator.

## License

MIT License

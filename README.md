# Neons log collector

[![CircleCI](https://circleci.com/gh/NeonsIo/collector.svg?style=svg)](https://circleci.com/gh/NeonsIo/collector)

## Goals 

Log collector is responsible for receiving request from [javascript tracker](https://github.com/neonsio/tracker) preparing logs and sending it to sink.  Currently it is supporting [kafka](https://kafka.apache.org/) sink.

## Run application

To run application set up configuration placed in [src/main/resources/application.conf](https://github.com/NeonsIo/collector/blob/master/src/main/resources/application.conf) and then use [SBT](www.scala-sbt.org) to run application

```
sbt run
```

## License

License can be found [here](https://github.com/NeonsIo/collector/blob/master/LICENSE).

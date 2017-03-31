# Neons log collector

[![CircleCI](https://circleci.com/gh/NeonsIo/collector.svg?style=svg)](https://circleci.com/gh/NeonsIo/collector)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/b629773816d446c2bf419f9c7de9d192)](https://www.codacy.com/app/michalsikora/collector?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=NeonsIo/collector&amp;utm_campaign=Badge_Grade)

## Goals 

Log collector is responsible for receiving request from [javascript tracker](https://github.com/neonsio/tracker) preparing logs and sending it to sink.  Currently it is supporting [kafka](https://kafka.apache.org/) sink.

## Run application

To run application set up configuration placed in [src/main/resources/application.conf](https://github.com/NeonsIo/collector/blob/master/src/main/resources/application.conf) and then use [SBT](www.scala-sbt.org) to run application

```
sbt run
```

## License

License can be found [here](https://github.com/NeonsIo/collector/blob/master/LICENSE).

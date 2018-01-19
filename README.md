# README #

[![Build Status](https://travis-ci.com/ONSdigital/address-index-api.svg?token=wrHpQMWmwL6kpsdmycnz&branch=develop)](https://travis-ci.com/ONSdigital/address-index-api)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/83c0fb7ca2e64567b0998848ca781a36)](https://www.codacy.com/app/Valtech-ONS/address-index-api?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=ONSdigital/address-index-api&amp;utm_campaign=Badge_Grade)

### What is this repository for? ###

* Address Index is a Play Framework application which resolves address query strings to actual addresses.
* Version 0.0.1

### How do I get set up? ###

* Install `java`
* Install `sbt`
* Run `sbt -Des.path.home="location/to/store/es/data/if/local"` or just `sbt` if you're using a remote elasticsearch
* For Windows put double quotes around complete args string e.g. `sbt "-Des.path.home=C:\\\es\\\data"`
* `project address-index-server`
* `re-start` utilise sbt-revolver, we have some eager singletons which load share objects
* Go to web browser `localhost:9000`

### Running Instructions (Docker)

Firstly, start an ElasticSearch container.

```shell
docker pull elasticsearch:2.4
docker run -p 9200:9200 -p 9300:9300 -e "discovery.type=single-node" elasticsearch:2.4
```

Publish the docker images for the API and UI:

```shell
sbt docker:publishLocal
```

Run the api:

```shell
docker run address-index-server:0.0.1 -DONS_AI_API_ES_URI=172.17.0.2 -DONS_AI_API_ES_PORT=9200 -DONS_AI_API_ES_CLUSTER_NAME=elasticsearch
```

Run the ui:

```shell
docker run -p 9000:9000 address-index-demo-ui:0.0.1
```


### Contribution guidelines ###

* Scaladoc
* Pull requests must have tests

### Who do I talk to? ###

* Richard Smith

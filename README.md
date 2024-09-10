# shop-poc-quarkus - Istruzioni di avvio del progetto



## Running the application in dev mode

You can run your application in dev mode that enables live coding using:

```shell script
./mvnw compile quarkus:dev
```

> **_NOTE:_**  Quarkus now ships with a Dev UI, which is available in dev mode only at <http://localhost:8080/q/dev/>.

## Packaging and running the application

The application can be packaged using:

```shell script
./mvnw package
```

It produces the `quarkus-run.jar` file in the `target/quarkus-app/` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `target/quarkus-app/lib/` directory.

The application is now runnable using `java -jar target/quarkus-app/quarkus-run.jar`.

## build Container Immage using JVM

The Container Image can be build using dockerfile.jvm:

```shell script
docker build -f src/main/docker/Dockerfile.jvm -t shop-poc-quarkus-jvm:latest .
```

## Run Container Immage 


```shell script
docker run -i --rm -p 8080:8080 shop-poc-quarkus-jvm
```

## Tag Container Immage and push on AWS Elastic Repository

```shell script
docker tag shop-poc-quarkus-jvm:latest 905418236103.dkr.ecr.eu-central-1.amazonaws.com/shop-poc-quarkus:latest
```


```shell script
docker push 905418236103.dkr.ecr.eu-central-1.amazonaws.com/shop-poc-quarkus:latest
```


## Creating a native executable without GraalVM in local

You can create a native executable using:


```shell script
./mvnw package -Pnative --define quarkus.native.container-build=true  
```


## Creating a native container without GraalVM in local

```shell script
./mvnw package -Pnative  --define quarkus.native.container-build=true  --define quarkus.container-image.build=true  --define quarkus.container-image.name=<your-image-name>    --define quarkus.container-image.tag=<your-tag>
```

## build a native Container Image if you have GraalVM



```shell script
./mvnw package -Pnative 
```
then build container image

```shell script
 docker build -f src/main/docker/Dockerfile.native -t quarkus/shop-poc-quarkus .
```

## Run the container using:

```shell script
docker run -i --rm -p 8080:8080 quarkus/shop-poc-quarkus
```


# Entita principali

non avendo un perimetro ben definito e' stata scelta la seguente struttura di dati per la nostra implementazione.

aggiungo il link a un Entity Diagram:
https://drive.google.com/file/d/1qHY0IANSjz4wkIvHKE8i5o7_6nkwk9JA/view?usp=sharing
# Nota

La tipologia di ID in alcune entita e' stato semplificato usando Long

## shop

l'entita' Shop rappresenta il Magazzino/Store esselungo sui cui vengono associati i carrelli, Prodotti e ordini



## Cart

l'entita' Cart rappresenta la spesa del cliente, ogni cliente puo avere N Carrelli con un massimo di 1 Carrello in stato "Draft" per ogni Store

## cliente

il cliente come anticipato puo avere N carrelli gestiti tramite lo Stato, N indirizzi Gestiti tramite il campo isDefault "come indirizzo principale".

la scelta di N carrelli pensata per permettere il multi acquisto da differenti store in base ai prodotto disponibile di esso, prenotare/schedulare ordini 
futuri senza mantenendo ancora il carrello attivo.

## Order

l'Entita' Ordine viene utilizzata per permettere il collegamento di altre entita' di natura differente dal semplice carrello, come:

-Indirizzo di spedizione

-Transazioni di pagamento

-Vari WorkOrder sull'ordine

-altre logiche di calcolo costi aggiuntivi ecc...


# Supposizioni Generali 


## Servizi Esposti

Generalmente il flusso corretto di invocazione e' il seguente

-	Creare un cliente

-	Creare un Carrello usando uno dei due shop gia Definiti 1 e 2

-	Creare un ordine 

-	invocare Order/{id}/pay

-	invocare shop/{id}/Delivery


## Servizio di creazione di una spesa

### Post   /carts

la logica nella creazione della spesa(carrello), passa per diversi step

controllo che il cliente non ho un carrello in stato "Draft" per lo store indicato.
	- se esiste un carrello, si verifica il prodotto se presente nel carrello si aggiunge la quantita', in alternativa aggiunge un prodotto
	- se non esiste un carrelle si crea uno nuovo e si aggiungono i prodotti 

ClientID utilizzabile: 1

## Servizio Rest di modifica spesa

### Put /orders/{OrderId}

la logica e' stata pensata sull'entita' Ordine, una volta che il cliente vorrebbe procedere all'ordine, 
deve essere invocato il servizio Post "/Orders" per creare un Ordine, all'ordine creato deve essere associato:

1- un carrello, per la lista della spesa.
2- un cliente per popolare l'inderizzo di consegna "OrderAddress" dall'indirizzo del cliente con isDefault=true

viene dato uno stato "Draft" anche all'ordine in questa fase, finche non si effettua un pagamento tramite il servizio GET Orders/{orderId}/pay

una volta invocato tale servizio, lo stato del carrello passa a "OnDelivery" e lo stato dell'ordine passa a "PAID".
differenziando gli stati del Ordine e il carrello ci permette di avere piu'flessibilita' sulle operazioni possibili da fare su entrambi.

- in particolare come richiesto, l'indirizzo di consegna dell'ordine associato ad un carrelo in uno stato diverso da "DRAFT" non puo essere modificat.


	
## Servizio Rest per la lista delle spese permettendo di filtrare

### Get /orders/filter?date={value}&status={value}

la logica si base sempre sugli ordini dando la possibilita' di filtrare per Data e Stato dell'ordine


	
## Servizio Rest per la creazione della rotta di consegna degli ordini

### Get /shops/{id}/delivery?date={value}


trattandosi di un servizio multi shop il servizio richiede l'id del SHOP per recuperare tutti gli ordini in stato "PAID" in un determinato giorno e creare la Rotta Miglior

### Algoritmo di ordinamento

Sapendo che il problema posto e' un problema con soluzioni solo teoriche in quanto il numero delle compinazioni e' esponenziale in base al numero degli ordini.
visto che il problema e' noto, facendo diverse ricercha e' stato utilizzato un algoritmo simile al BFS Algorithm, con una differenza che non si scarta nessun nodo.


## Beans
per la semplicita' del progetto e in mancanza di Sessione non e' stato neccessario definire dei bean di tipo @RequestScoped, @SessionScoped e @Dependent


# Documentazione, Deploy e Testing

## Swagger

Lo swagger del progetto si potrebbe consultare nel seguente link

https://gsshhjgzyn.eu-central-1.awsapprunner.com/swagger-ui/

## Container Registry
 
905418236103.dkr.ecr.eu-central-1.amazonaws.com/shop-poc-quarkus:latest

## Deployment

il servizio e' deployed on App runner di AWS, e' possibile invocarlo tramite il seguente Url:

https://gsshhjgzyn.eu-central-1.awsapprunner.com/

## Sonarlint

sono state risolte tutte le issue su sonarlint


## testing della soluzione

### SHOP
come shop id e' possibile usare i seguenti valori

-	1
-	2

### Date

come data disponibile si potrebbe usare "2024-09-10"
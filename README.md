# SYNC TO AND FROM TRELLO
Questo applicativo serve per sincronizzare un database in locale con una board di trello, offre controller basici di modifica insieme a un controller `synchronization` che offre due metodi, uno di sincronizzazione
da trello verso il database, e uno che sincronizza il database verso trello.

# FUNZIONAMENTO
Dopo aver inserito la board id tramite comando sql nella tabella config, e token e key dentro la tabella api_key, il programma svolgera potrà svolgere la sincronizzazione.

## Sincronizzazione da trello
Il programma necessita di una prima sincronizzazione verso trello, questo aggiungerà ogni label (etichetta), members, commento, lista, e card insieme alle checklist dentro essa. Prenderà l'id assegnato da trello a ogni entità
in modo da svolgere update nella fase di sincronizzazione verso trello.

## Sincronizzazione verso trello
Il programma svolge una sincronizzazione verso trello, per ogni entità presente nel database locale controllerà se è necessario fare una richiesta di post per entità nuove senza un trello id, e invece una richiesta di put
entità dove la colonna trello id ha un valore valido. NON SVOLGERE UNA SINCRONIZZAZIONE VERSO TRELLO A DATABASE VUOTO, la sincronizzazione verso trello compara se fare un update o un post in base
all'ultima modifica prendendo come riferimento il database vuoto. Se svolto a database vuoto la board su trello verrà svuotata.

## Gestione conflitti
In entrambi fasi di sincronizzazione, prima di fare un update tramite post o una modifica al database, i metodi hanno implementato un controllo sull'ultima modifica, se per esempio svolgo una modifica nel database
locale, ma la board di trello ha avuto una modifica più recebte e faccio una sincronizzazione verso trello, la mia modifica verso la board non verrà svolta. Viceversa si applica in una sincronizzazione da trello verso
al databse, dove se una modifica svolta su trello risulta meno recente di una modifica svolta nel database locale, la modifica che soppravale in fase di sincronizzazione verso trello
sarà la modifica svolta nel database locale dato che è la più recente.

# LIMITAZIONI
- Alcuni campi su trello non hanno un modo per comparare l'ultimo aggiornamento, per esempio le liste su trello non hanno un field che segna quando è stato modificato il nome. Quindi in fase di sincronizzazione vincerà sempre la modifica, a differenza dei sottocampi come checklist dove l'ultima modifica viene presa dalla card a cui appartengono
- I commenti potranno essere modificati solo dall'user a cui appartiene la key e il token, se un commento viene modificato in database locale ed è stato scritto da un user di cui l'api key non appartiene, la modifica non verrà svolta

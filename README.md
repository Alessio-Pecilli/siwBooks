## Casi d'Uso

Di seguito sono elencati i principali **casi d’uso** del sistema, con indicazione dell’attore primario coinvolto e delle operazioni consentite.

### UC1 – **Visualizzazione libri**  
**Attore primario**: Utente occasionale  
L’utente occasionale utilizza il sistema per consultare l’elenco dei libri. Il sistema mostra i dettagli relativi a ciascun libro (titolo, descrizione, data, ecc.).

### UC2 – **Visualizzazione recensioni**  
**Attore primario**: Utente occasionale  
L’utente occasionale utilizza il sistema per consultare le recensioni associate ai libri. Il sistema mostra tutte le recensioni scritte dagli utenti registrati.

### UC3 – **Visualizzazione autori**  
**Attore primario**: Utente occasionale  
L’utente occasionale utilizza il sistema per consultare le informazioni relative agli autori. Il sistema mostra i dati dell’autore e i libri da lui scritti.

### UC4 – **Scrittura recensione**  
**Attore primario**: Utente registrato  
L’utente registrato utilizza il sistema per aggiungere una recensione a un libro. È possibile aggiungere **una sola recensione per ciascun libro**. Il sistema associa la recensione all’utente e al libro selezionato.

### UC5 – **Modifica o eliminazione recensione personale**  
**Attore primario**: Utente registrato  
L’utente registrato utilizza il sistema per modificare o eliminare **una recensione scritta da lui stesso**. Il sistema mostra l’elenco delle recensioni personali e consente la selezione di quella da modificare o cancellare.

### UC6 – **Aggiunta libro**  
**Attore primario**: Amministratore  
L’amministratore utilizza il sistema per aggiungere un nuovo libro. Il sistema registra il libro con tutte le informazioni inserite.

### UC7 – **Modifica o eliminazione libro**  
**Attore primario**: Amministratore  
L’amministratore utilizza il sistema per modificare o eliminare un libro esistente. Il sistema consente l’aggiornamento dei dati del libro oppure la sua cancellazione.

### UC8 – **Aggiunta autore**  
**Attore primario**: Amministratore  
L’amministratore utilizza il sistema per aggiungere un nuovo autore. Il sistema registra i dati dell’autore e li rende disponibili per l’associazione ai libri.

### UC9 – **Modifica o eliminazione autore**  
**Attore primario**: Amministratore  
L’amministratore utilizza il sistema per modificare o eliminare un autore esistente. Il sistema consente l’aggiornamento dei dati dell’autore oppure la sua rimozione.

### UC10 – **Eliminazione recensione**  
**Attore primario**: Amministratore  
L’amministratore utilizza il sistema per eliminare una qualsiasi recensione presente. Il sistema consente di selezionare una recensione e rimuoverla. **Non è consentita la modifica da parte dell’amministratore.**

---

### Riepilogo Casi d'Uso

| Codice | Titolo                                | Attore primario     |
|--------|---------------------------------------|----------------------|
| UC1    | Visualizzazione libri                 | Utente occasionale   |
| UC2    | Visualizzazione recensioni            | Utente occasionale   |
| UC3    | Visualizzazione autori                | Utente occasionale   |
| UC4    | Scrittura recensione                  | Utente registrato    |
| UC5    | Modifica o eliminazione recensione    | Utente registrato    |
| UC6    | Aggiunta libro                        | Amministratore       |
| UC7    | Modifica o eliminazione libro         | Amministratore       |
| UC8    | Aggiunta autore                       | Amministratore       |
| UC9    | Modifica o eliminazione autore        | Amministratore       |
| UC10   | Eliminazione recensione               | Amministratore       |

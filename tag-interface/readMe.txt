Guida all'utilizzo dell'interfaccia

Interfaccia POS Tagging

La PRIMA COSA DA FARE è inserire la firma dell'annotatore

Per annotare la frase, cliccare sui bottoni ADJ,ADP ecc. La parola alla quale viene applicato il tag è quella illuminata di azzurro.
Per esprimere poi il sentiment della frase, cliccare sui bottoni Positivo,Negativo o Neutro

Cliccando sul bottone "Save and next" si passa alla frase successiva. La parola "Save" si riferisce alla memoria interna del programma
Il bottone "Revert" permette di ri-annotare dall'inizio la frase
Il bottone "Go Back" permette di tornare indietro alla frase precedente
NB: Il bottone "Go Back" NON torna indietro all'ultima frase annotata, ma alla frase precedente nel dataset. Questo vuol dire che se faccio un Jump to alla riga 10, Go Back mi farà tornare alla terza colonna della riga 13
Il bottone "Jump to" permette di saltare alla riga indicata alla sua destra

Il bottone "SAVE TO FILE!" permette di salvare le frasi annotate nel file dataset.json
il bottone "SAVE TO EXTERNAL FILE" permette di salvare le frasi annotate nel file posOutput.json

Differenze tra i file dataset.json e posOutput.json

La differenza tra questi due file .json è che in dataset.json sono presenti TUTTE le frasi, eventualmente non annotate.
In posOutput.json, invece, sono presenti solo ed esclusivamente le frasi annotate durante la sessione nella quale si è cliccato sul bottone "SAVE TO EXTERNAL FILE"


Interfaccia Voto

In questa interfaccia la firma annotatore va inserita solo la prima volta
Se volete azzerare ( o modificare ) il counter, aprite config.txt e modificate manualmente il numero accanto a "count:"
Non è necessario modificare la firma dal file config.txt, basta inserirla dall'interfaccia

Il tasto "CARICA LISTA COPPIE" carica le 50 coppie di righe elencate nel file listaRighe0. Per caricare una lista diversa da quella, rinominare la lista desiderata in "listaRighe0". Per scorrere le coppie nella lista caricata utilizzare il tasto "Save and next IN LIST".

Il voto si esprime cliccando sui bottoni "1: Molto diverse", "2: Diverse" ecc.
Accanto alla label "Inizio e fine offset (Riga)" è possibile impostare il range dal quale estrarre le coppie di frasi
Accanto alla label "Colonna" è possibile selezionare la colonna dalla quale vengono prese le coppie di frasi. Gli indici delle colonne sono 0,1 e 2.
NB: Questi 3 parametri vanno impostati contemporaneamente
Il bottone Set permette di settare i 3 parametri scelti
Esempio di utilizzo: imposto come offset 1-50 e come colonna "0". Quando cliccherò su "Save and next" l'interfaccia mi proporrà coppie di frasi della stessa lezione prese tra le righe 1 e 50 dalla colonna 0 (Argomento meno chiaro)

Il bottone Jump permette di selezionare una coppia specifica di frasi scegliendo riga della prima frase, riga della seconda frase e colonna delle due frasi

Il bottone "Save and next RANDOM" salva nella memoria del programma il voto appena espresso e passa alla coppia di frasi successiva randomicamente

Il bottone "Save to file" salva sul file semantics.json i voti espressi


NB: Se per qualche motivo avete bisogno di generare un dataset vergine (non annotato), potete farlo eseguendo dall'IDE il main di RegenerateCleanDataset (che trovate dentro il package dataProcessing)
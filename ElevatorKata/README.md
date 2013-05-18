ElevatorKata
============

Zaimplementuj klase widny (Elevator). Wymagania:

1. Winda zaczyna na parterze,
2. Winda moze znajdowac sie w jednym ze stanow: "jedzie w gore", "jedzie w dol", "czeka", "wymaga naprawy",
3. Winda po nacisnieciu przycisku zamyka drzwi, po zamknieciu drzwi uruchamia silnik,
4. Winda po osiagnieciu zadanego pietra zatrzymuje silnik i otwiera drzwi,
5. Winda odwiedza pietra po kolei (jezeli klikniete zostana 4,2,5, to zatrzyma sie kolejno 2,4,5),
6. Winda nie zmienia kierunku (jezeli jestesmy na 2 i jedziemy na 4 i na 3 pietrze wciskamy 1 to nadal jedziemy na 4),
7. Jezeli silnik sie zepsuje winda sie zatrzymuje i przechodzi w stan "Wymaga serwisowania",
8. Winda z stanie "Wymaga serwisowania" nie reaguje na naciskanie przyciskow.
9. Jeżeli widna wykryje awarie czujnika (np. odwiedzi pietro 3 a pozniej 5 z pominieciem 4) przechodzi w stan "Wymaga Serwisowania".

# Algorytmy-systemów-operacyjnych

Projekty zrealizowane w ramach kursu Systemy Operacyjne. Celem symulacji porównanie klasycznych algorytmów:

* planowania dostępu do procesora (CPU Scheduling)
* planowania dostępu do dysku (Disk Scheduling)

Program pozwala analizować ich wydajność na podstawie średniego czasu oczekiwania procesów oraz sumarycznych przemieszczeń głowicy dysku.
Dodatkowo każda z symulacji samodzielnie generuje zbiory danych.

## Zadanie 1 - Planowanie CPU

**Cel**
Symulacja i porównanie algorytmów planowania procesora:

* FCFS (First Come First Served)
* SJF (Shortest Job First) – wersja wywłaszczająca i niewywłaszczająca
* Round Robin (RR) z możliwością konfiguracji kwantu czasu

**Parametry konfiguracyjne**

* liczba procesów w zbiorze
* maksymalny czas procesu
* wielkość kwantu czasu dla algorytmu RR

**Mierzone parametry**

* Czas pracy
* Średni czas oczekiwania
* Maksymalny czas oczekiwania
* Liczba zagłodzonych procesów
* Liczba przełączeń

## Zadanie 2 - Planowanie dostępu do dysku

**Cel**
Symulacja algorytmów planowania ruchu głowicy dysku oraz analiza ich efektywności.

* FCFS (First Come First Served)
* SSTF (Shortest Seek Time First)
* SCAN
* C-SCAN
Algorytmy z obsługą real-time
* EDF
* FD-SCAN

**Parametry konfiguracyjne**

* rozmiar zbioru żądań
* rozmiar dysku
* pozycja startowa

**Mierzone parametry**

* Średni czas oczekiwania
* Maksymalny czas oczekiwania
* Liczba zagłodzonych procesów (dla zbiorów z żądaniami real-time)
* Liczba przesunięć

## Zadanie 3 - Algorytmy zastępowania stron

**Cel**
Symulacja algorytmów zastępowania stron oraz analiza ich efektywności.

* FIFO
* OPT
* LRU
* Aproksymacja LRU
* RAND

**Parametry konfiguracyjne**

* liczba dostępnych ramek
* liczba stron w procesie
* parametry do obliczania szamotania

**Mierzone parametry**

* Liczba błędów stron
* Liczba szamotań

## Zadanie 4 - Algorytmy przydziału ramek do procesów

**Cel**
Symulacja algorytmów przydzielania ramek do procesów.

* przydział proporcjonalny
* przydział równy
* sterowanie częstością błędów
* model strefowy

**Parametry konfiguracyjne**

* liczba dostępnych ramek
* wygenerowane przez program procesy z parametrami

**Mierzone parametry**

* Suma błędów stron
* Suma szamotań
* Suma błędów oraz ostateczna liczba ramek poszczególnych procesów 
* Liczba uśpionych procesów






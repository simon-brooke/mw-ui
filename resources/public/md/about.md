## What is MicroWorld

MicroWorld is a rule driven cellular automaton.

### Representation of the world

A world is represented as a
sequence of sequences (to represent a two dimensional array) of cells. A cell is
represented as a map having at least the following properties:

* **x** The X offset of this cell within the array (should not be tampered with).
* **y** The Y offset of this cell within the array (should not be tampered with).
* **state** The current state of this cell, represented as a clojure key; initially :waste.

Rules can add any additional properties to cells that you like; the current
'ecology' rule-set adds properties for the populations of deer and of
wolves.

### Representation of rules

Rules are simple production rules, in a [grammar](docs#grammar) defined in the 
documentation. Each rule is compiled into a Clojure anonymous function of two 
arguments, **world** and **cell**, which if its conditions are met returns a 
new cell which must the same **x** and **y** values as the cell which was 
passed. Any other properties may be modified.

## What is MicroWorld for

The underlying intention is to crudely model the evolution of human settlement
in a landscape, but also to act as a learning environment to teach ideas about
software and about geography and ecology.

## User interface

The MicroWorld user interface is currently just an auto-refreshing web page,
containing an HTML table. Each cell in the world is represented by a cell in
the table. Each HTML cell has a class whose name is derived from the state of
the world cell, and contains an image whose filename is derived from the state
of the world cell.

The 'state' classes are defined in a CSS file 'states.css', which should be
edited when you add additional states. Image files should be 32x32 pixels and
should be stored in the directory 'img/tiles', be in PNG format, and have a
name comprising the name of the state followed by '.png'.

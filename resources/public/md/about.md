## What is MicroWorld

MicroWorld is a rule driven cellular automaton.

### Representation of the world

A world is represented as a
sequence of sequences (to represent a two dimensional array) of cells. A cell is
represented as a map having at least the following properties:

* **x** The X offset of this cell within the array (should not be tampered with).
* **y** The Y offset of this cell within the array (should not be tampered with).
* **state** The current state of this cell, represented as a clojure key; initially :waste.

As currently initialised all cells have two additional properties:

* **altitude** Altitude of this cell. Initially 1. Currently not used.
* **fertility** Soil fertility of this cell. Initially 1. Increases under climax forest; later, will decrease under ploughland, may increase under pasture.

Rules can add any additional properties to cells that you like; the current
'natural-rules' rule-set adds properties for the populations of deer and of
wolves.

### Representation of rules

Rules are just ordinary Clojure functions (can be anonymous functions, and in the current
rule-sets they are) which take two arguments, a cell and a world, and return
either nil (indicating the rule did not fire), or a new cell which should have
the same :x and :y properties as the cell passed in. Any other properties can
be different.

## What is MicroWorld for

The underlying intention is to crudely model the evolution of human settlement
in a landscape, but also to act as a learning environment to teach ideas about
software and about ecology.

## User interface

The MicroWorld user interface is currently just an auto-refreshing web page,
containing an HTML table. Each cell in the world is represented by a cell in
the table. Each HTML cell has a class whose name is derived from the state of
the world cell, and contains an image whose filename is derived from the state
of the world cell.

The 'state' classes are defined in a CSS file 'states.css', which should be
edited when you add additional states. Image files should be 32x32 pixels and
should be stored in the directory 'img/tiles', but at this stage I haven't
created any.

## Future plans

### Run in the browser

Currently the simulation runs on the server and only the display is in the
browser. It would be much better converted to ClojureScript and run in the browser
- that would save both network traffic and server load, and would allow the
animation to run faster.

### Rule language

It would be good to have a near-English rule parser to make rules easier for
children to add and modify.

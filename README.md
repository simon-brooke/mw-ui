## What this is about

MicroWorld is a rule driven cellular automaton. What does that mean? Well, it's
a two dimensional world made up of squares called **cells**. The world develops
in steps, and at each step, each cell is modified by applying the rules.

[Go and watch](world) it running for a few moments, then come back here.

The demonstration world is a mountain, with snow at the top and the sea at the 
bottom. as you watched, you probably saw the bright green of grass on the lower
slopes of the mountain turn to the darker green of forest. You may have seen
some forest fires break out.

That's all controlled by rules. You make the rules. To start Noah's flood, 
[go to the rules page](rules) now, and add this rule at the very top:

    if altitude is less than 200 then state should be water

then, [go and watch the world](world) again. What happens? You should see water
spread across everywhere except the very top of the mountain. But after the 
flood, the waters should drain away again. Go back to [rules](rules) and add 
this rule at the very top:

	if altitude is more than 9 and state is water then state should be grassland

Now the world alternates between *new* and *grassland*. That's no good! Go back to
[rules](rules) and delete the rule that you first added - the one that says

    if altitude is less than 200 then state should be water

And see! The world starts growing again.

## What you can do next

### Change some rules

Change some of the other rules and see what happens. Very likely, one of the 
first things that will happen is that you will get a message like this:

    I did not understand 'if state is grassland then 1 chance in 10 state will be heath'

That means that you changed a rule in a way that the engine could no longer 
understand it. To find out what the engine will understand, have a look at the
[documentation](docs#grammar).

### Invent some rules of your own!

What happens when people come into the world? Where would they make their first 
camp? Would they want to be near the water, so they could fish? Would they want 
be near fertile grassland, to graze their sheep and cattle?

__Write a rule which adds some camps to the world__ 

What happens to the land around a camp? Do the people burn down forest to make 
new grassland? Do they convert the grassland into meadow, or into crop?

Does growing crops reduce the soil fertility? What makes people decide that their
camp is a good enough place to build a proper house?

__Write some rules which describe this__

How many squares of meadow or crop does it take to feed each house full of people?
What happens when there are too many houses and not enough fields? Can houses 
catch fire? What happens to a house which is next to a fire?

How many houses do you need for a market place? Where would people build a
harbour?

### Change the rules completely

I've provided rules which use the MicroWorld cellular automaton to make a simple
model of the changes to land in Europe after the ice age. But you don't have to
use it like that, at all.

[Conway's Game of Life](http://en.wikipedia.org/wiki/Conway's_Game_of_Life) is one
of the famous uses of a cellular automaton. The rules for the Game of Life are
very simple. To set up your game of life you'll need some initialisation rules,
one for every cell you want to start live (we'll use _black_ for live, and 
_white_ for dead):

	if x is equal to 4 and y is equal to 4 and state is new then state should be black

Add as many of these as you need for your starting pattern. Then add a rule, after
all those:

    if state is new then state should be white

I'll leave you to work out what the rules of life are for yourself, from the
Wiki page I linked to.

### Change the engine

If you want to modify the engine itself, you will need 
[Leiningen](https://github.com/technomancy/leiningen) 2.0 or above installed on
your own computer, and you'll need to download the source, and unpack it. 

You will find that there are three packages:

+ __mw-engine__ deals with creating worlds, and transforming them;
+ __mw-parser__ deals with turning the rule language into something the engine can work on;
+ __mw-ui__ is the web site which provides you with a user interface. 

For each of these packages, you need to run, in the root directory of the package, 
the following command:

    lein install

Once you've done that, you'll have everything built. To start a web server for 
the application, run the following command in the _mw-ui_ directory:

    lein ring server

Now you have it working, you can start changing things. 

#### Add states

Adding new states is easy:
just add new tiles in _mw-ui/resources/public/img/tiles_. The tiles should be 
png (Portable Network Graphics) files, and should be 32 pixels square. The name
of the tile should be the name of your new state. It's good to also edit the file
_mw-ui/resources/public/css/states.css_ to add a class for your new state.

#### Change the code

Once you've done all that, you're ready to be really ambitious. Change the code.
Implement some new feature I haven't thought of, or fix bugs I've accidentally
left in.

You'll need an editor. I recommend either [NightCode](https://nightcode.info/),
which is quite small and will run on a Raspberry Pi, or 
[LightTable](http://www.lighttable.com/), which is extremely helpful but needs
a more powerful computer.

Have fun!

## License

Copyright © 2014 [Simon Brooke](mailto:simon@journeyman.cc)

Distributed under the terms of the [GNU General Public License v2](http://www.gnu.org/licenses/gpl-2.0.html)
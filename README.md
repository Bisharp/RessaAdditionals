# RessaAdditionals

Source: Contains the scripts I'd made before to help with ReSSA development. They handle minifying scripts to paste into the JSON structures that ReSSA 1.0 takes, as well as a primitive utility for expanding from one-liners with some indentation. This is NOT PERFECT (`match` statements broke, among others), though. Given how the updated Prophet system takes in a file of valid Rune code for scripts and grafts them into the JSON schema, this will likely (and thankfully) become obsolete once that update is done.

The `RunePlaygroundTemplate.txt` file contains a set of code that can be copy/pasted into [the Rune playground](https://rune-rs.github.io/play/?c=cHViIGZuIG1haW4oKSB7fQo%3D) to create an environment condusive to developing ReSSA callbacks. This has a mock context implementation and well-defined locations for placing test data, the script you're developing, etc.

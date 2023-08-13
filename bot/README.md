# Simulator Bot

A Discord Bot to let you simulator Tower of Fantasy's banner orders.

## Commands

**/banner create <name> \[theory\]**

Create a new banner of with a given name. See [theory](#theory) about the `theory` option.

**/banner pull <amount> \[name\] \[theory\]**

Pull on the banner a certain amount of times. If the amount is a negative number (-1) pull until you have gotten 7
limited weapons (A6). If you did not create a banner with the **/banner create <name>** command, you will get an error.
Use the `name` option to create or override your current banner. See [theory](#theory) about the `theory` option.

**/banner reset**

Reset your current banner. If you did not create a banner wth the **/banner create <name>** command, you wll get an
error.

### Theory

Currently, there is a popular theory on the ToF banners which states you cannot win nor loose 50/50 more than 2 times
in a row. So when you lost the hard pity (80) to a standard SSR weapon, and you got another standard weapon when
building pity. Your next SSR will be the limited weapon of the banner you're pulling on.

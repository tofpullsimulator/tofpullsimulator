# Simulator Bot

A Discord Bot to let you simulator Tower of Fantasy's banner orders.

## Commands

**/banner create-matrix <name> \[theory\]**

Create a new matrix banner of with a given name. See [theory](#theory) about the `theory` option.

**/banner create-weapon <name> \[theory\]**

Create a new weapon banner of with a given name. See [theory](#theory) about the `theory` option.

**/banner pull-weapon <amount> \[name\] \[theory\]**

Pull on the weapon banner a certain amount of times. If the amount is a negative number (-1) pull until you have gotten
7 limited weapons (A6). You can either pull the limited weapons by chance, or buy them for 120 flame gold. The buying
of the limited weapons happens automatically. So the flame gold will not go above 120. If you did not create a weapon
banner with the **/banner create <name>** command, you will get an error. Use the `name` option to create or override
your current banner. See [theory](#theory) about the `theory` option.

**/banner pull-matrix <amount> \[name\] \[theory\]**

Pull on the matrix banner a certain amount of times. If the amount is a negative number (-1) pull until you have gotten
4 pieces of each matrix, so 16 in total. You can either pull the matrices or buy them from the shop. The buying only
happens at the end of the banner, but the bot keeps track on which pieces you could buy. Furthermore the matrix banner
assumes you have bought the matrix box (no additional cost is added to the banner). Lastly the banner has a simple 
blocking mechanism for all the available matrices. When you have 4 of the same matrix piece this one gets blocked. If 
you have multiple matrix pieces at 4 it blocks the first one in alphabetical order. If you did not create a matrix
banner with the **/banner create <name>** command, you will get an error. Use the `name` option to create or override
your current banner. See [theory](#theory) about the `theory` option.

**/banner reset**

Reset your current banner. If you did not create a banner wth the **/banner create <name>** command, you wll get an
error.

### Theory

Currently, there is a popular theory on the ToF banners which states you cannot win nor loose 50/50 more than 2 times
in a row. So when you lost the hard pity (80) to a standard SSR weapon, and you got another standard weapon when
building pity. Your next SSR will be the limited weapon of the banner you're pulling on.

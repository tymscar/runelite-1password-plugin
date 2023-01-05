![](https://github.com/tymscar/runelite-1password-plugin/blob/main/media/runelite-icon.png) ![](https://github.com/tymscar/runelite-1password-plugin/blob/main/media/onepassword-icon.png)
# 1Password
This plugin talks to the [1Password](https://1password.com/) CLI client and autocompletes your login details in [Runelite](https://runelite.net/)!

Before you start, make sure: 

* You have the [1Password CLI](https://developer.1password.com/docs/cli/get-started/#install) installed 
* `op` is accessible in your PATH 
* You are comfortable using a command line interface
* All your [Runescape](https://oldschool.runescape.com/) accounts have each a website entry of `https://runescape.com`


# Usage

There are two slightly different usecases for the plugin:


## If you only have one account

This is the most common usecase. It means you have only one Runescape account saved in your 1Password vault.
To get it running all you have to do is:

1. Install the [1Password CLI](https://developer.1password.com/docs/cli/get-started/#install). Make sure to follow all the steps and login.
2. Install this plugin. You probably already did it considering you are reading this.
3. Open up Runelite
4. A popup will appear asking you to unlock your vault. On Windows this will be Windows Hello, on macOS it will be Touch ID and on Linux it will be a password prompt.
5. After a second or two, your username and password will be filled in for you and you can just log in!

Here is a gif with the one account flow in action:
![](https://github.com/tymscar/runelite-1password-plugin/blob/main/media/OneAccount.gif)

## If you only have two or more accounts

This is a less common usecase but still widely requested. It means you have multiple `alt` accounts that you want to quickly jump between and have 1Password automatically autocomplete their details.
To get this one running is very similar to the one account usecase, with one difference.
Here is all you have to do:

1. Install the [1Password CLI](https://developer.1password.com/docs/cli/get-started/#install). Make sure to follow all the steps and login.
2. Install this plugin. You probably already did it considering you are reading this.
3. Open up Runelite
4. A popup will appear asking you to unlock your vault. On Windows this will be Windows Hello, on macOS it will be Touch ID and on Linux it will be a password prompt.
5. After a second or two it will notice you have more than one Runescape account in your 1Password vault, so it will show a popup asking you to pick which one you want to login into!
6. As soon as you have selected your account it will ask you to confirm and unlock your vault again so it can retrieve the details
7. One it has the details, after a second or two it will fill the username and password in for you and let you log in!

Here is a gif with the multiple accounts flow in action:
![](https://github.com/tymscar/runelite-1password-plugin/blob/main/media/MultipleAccounts.gif)

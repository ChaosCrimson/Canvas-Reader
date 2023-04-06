# Canvas-Reader

This is the program for use with my grade retrieval system.


# Setup: A visual guide

In order to use this, a bit of user-guided preparation is required.

# Part 1: Generating an API Key

## Step 1:

Navigate to your Canvas homepage and click on your profile icon, then in the opened menu, click **Settings**

![alt text](https://github.com/ChaosCrimson/Canvas-Reader/blob/main/ReadmeImages/Step1.JPG?raw=true)

## Step 2:

Scroll down until you reach the button labeled **New Access Token**, and click on it.

![alt text](https://github.com/ChaosCrimson/Canvas-Reader/blob/main/ReadmeImages/Step2.JPG?raw=true)

## Step 3:

Put anything down on the purpose, that doesn't really matter. However, what does matter is that the expiration box **must be left blank**. Then click on generate token.

![alt text](https://github.com/ChaosCrimson/Canvas-Reader/blob/main/ReadmeImages/Step3.JPG?raw=true)


## Step 4:

I simply cannot empasize this enough. Save the generated token in a safe place. 

**DO NOT LOSE IT!**

> The resulting token should look something like this:
> 9327~AXXBBb0Ls9ReL8E709vsYUFHx542be89pc7MPSf1nZJO4ZwALperk0Z560qLzDpwT
> ^ (Not a real token (*I hope.*) )

# Part 2: Editing the input file

From the release, there should be a file in the **input**  folder called **InstructureStudentData.json**. Open it in notepad or your preferred text file editor.

The file contents should be as follows:

    [
      {
        "instructure_domain": "",
        "user_id": 0,
        "email":"",
	    "token":"",
        "frequency": "",
        "courses": [
          {
            "course_name": "",
            "course_id": "",
            "grading_period": ""
          }
        ]
      }
    ]
This is where you should input your own data.

Under **instructure_domain**, put your canvas page's domain within the quotes.

 - For me I would put jay<span>son.instructure.c</span>om

Under **email**, enter the email you would like to recieve notifications to.

Under **token**, paste in the API key you generated in Part 1

Finally, the **frequency** field. The content of this field will dictate whether you wish to recieve the updates every day or only on Sundays. For daily updates, put "**Daily**" in the field. For weekly updates, put "**Weekly**".

After that, just run the .exe file.

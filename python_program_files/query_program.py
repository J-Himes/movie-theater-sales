import sqlite3

def main():
    db = sqlite3.connect('movie_theater_sales.db')
    cursor = db.cursor()

    while True:
        printUserOptions()

        userInput = input()
        print("")

        if userInput.lower() == "quit":
            print("Connection closed. Goodbye!")
            break
        else:
            dbLookup(userInput, cursor)

    db.close()

    return

def printUserOptions():
    print("Please choose from the following list of options:\n")
    print("1) Enter \'theaters\' to view all available theaters")
    print("2) Enter \'movies\' to view all movie releases")
    print("3) Enter a date in the format MM/DD/YYYY to view theaters with the most sales for that date")
    print("4) Enter \'quit\' to exit the program\n")

def dbLookup(userInput, cursor):
    if userInput.lower() == "theaters":
        query = "SELECT * FROM Theaters;"
        cursor.execute(query)
        theaterInfo = cursor.fetchall()

        print("We have records for the following movie theaters:\n")

        for row in theaterInfo:
            print(f"{row[1]} in {row[2]}, {row[3]}.")
    elif userInput.lower() == "movies":
        query = "SELECT * FROM Movies;"
        cursor.execute(query)
        movieInfo = cursor.fetchall()

        print("We have records for the following movies:\n")

        for row in movieInfo:
            print(f"{row[1]}, released in {row[2]}.")
    else:
        userInput = padZeros(userInput)
        query = f'''WITH Sum_Sales AS ( 
                    SELECT SUM(sales) AS sales, theater_id 
                    FROM Sales 
                    WHERE date = ? 
                    GROUP BY theater_id
                    ),
    
                    Max_Sales AS (
                    SELECT theater_id
                    FROM Sum_Sales 
                    WHERE sales = (SELECT MAX(sales) FROM Sum_Sales)
                    )
    
                    SELECT name, city, state
                    FROM Theaters
                    WHERE id IN (SELECT theater_id FROM Max_Sales);
                '''

        cursor.execute(query, (userInput,))
        theaterInfo = cursor.fetchall()

        if len(theaterInfo) == 0:
            print("Invalid command or date.")
        else:
            print(f"The following movie theater(s) had the most sales on {userInput}:\n")

            for row in theaterInfo:
                print(f"{row[0]} in {row[1]}, {row[2]}.")

    print("")

    return

def padZeros(rawDate):
    finalDate = ""
    monthDayYear = rawDate.split('/')

    if len(monthDayYear) != 3:
        return rawDate

    monthDayYear[0] = monthDayYear[0].zfill(2)
    monthDayYear[1] = monthDayYear[1].zfill(2)
    monthDayYear[2] = monthDayYear[2].zfill(4)

    for i in range(0, 3):
        finalDate += monthDayYear[i]

        if i < 2:
            finalDate += '/'

    return finalDate

if __name__ == "__main__":
    main()
--- module (NICHT AENDERN!)
module PloyBot where
import Util
import Data.Int
import Data.Bits
import Data.Char
import Data.Char
import Data.List 
import Data.Word
import Data.Int


--- external signatures (NICHT AENDERN!)
getMove :: String -> String
listMoves :: String -> String

--- YOUR IMPLEMENTATION STARTS HERE ---
getMove nowState = (listOfMovesForAPlayer board player) !! 0
 where player = last nowState
       board = init(init nowState) 


listMoves boardAndPlayer = '[' : stringArrayToString ++ "]"
 where player = last boardAndPlayer
       board = init(init boardAndPlayer)
       stringArrayToString = intercalate "," (listOfMovesForAPlayer board player)




substring :: Int -> Int -> String -> String
substring start end text = take (end - start) (drop start text)

--gets a moveString and returns all rotations for it
--TESTED:SUCCESS
listOfRotatingForAMove :: String -> [String]
listOfRotatingForAMove moveString = [moveStartEnd ++ show rot| rot <- [0..7]]
 where moveStartEnd = init moveString

--gets a board string, position on board and a player and returns an array of all possible rotations
--of a figure on that position
--returns [] if there is no figure of the player on that position 
--TESTED:SUCCESS
listOfRotationMovesForAFigureForAPlayer :: String -> String -> Char -> [String]
listOfRotationMovesForAFigureForAPlayer board position player
 |(boardStringToList board  !! index) == "" = []
 | head (boardStringToList board  !! index) /= player = []
 |otherwise = [moveStartEnd ++ show rot| rot <-[1..7]]
  where moveStartEnd = position ++ "-" ++ position ++ "-"
        index =  fromIntegral (getIndexOfAPosition position)
 
--gets a board position and returns legit moves from that position
--all moves within 3 fields in all directions
--TESTED:SUCCESS
listOfAllMovesForAPosition :: String -> [String]
listOfAllMovesForAPosition position  = [moveStart ++ moveTarget ++ "-0"| moveTarget <- allPositions, abs(ord columnS - ord (moveTarget !! 0)) <= 3, abs(rowS - digitToInt (moveTarget !! 1)) <= 3]
 where allPositions = [[x] ++ show y | x <- ['a'..'i'],y <- [1..9]] -- ['x'] == "x"
       moveStart= position ++ "-" 
       columnS = position !! 0
       rowS = digitToInt (position !! 1)

--gets a board string, position on board and a player and returns all moves
--that follow the rules for that figure
--adds all the rotations of that move if it is a shield
--returns [] if there is no figure of the player on that position  
--TESTED:SUCCESS
listOfLawfullNonRotationMovesForAFigureForAPlayer ::String ->String -> Char -> [String]
listOfLawfullNonRotationMovesForAFigureForAPlayer board position player 
 |(boardStringToList board  !! index) == "" = []
 | head (boardStringToList board  !! index) /= player = []
 | whatFigure figureCode  == "shield" = concat [listOfRotatingForAMove move | move <- allMoves,canMoveAccordingToRules figureCode (fst (distanceAndDirectionOfTheMove move 0)) (snd (distanceAndDirectionOfTheMove move 0)) False]
 | otherwise  = [move | move <- allMoves,canMoveAccordingToRules figureCode (fst (distanceAndDirectionOfTheMove move 0)) (snd (distanceAndDirectionOfTheMove move 0)) False]
   where index = fromIntegral (getIndexOfAPosition position)
         figureCode = read (drop 1 (boardStringToList board  !! index))
         allMoves = listOfAllMovesForAPosition position

--TESTED:SUCCESS
listOfLawfillNonRotationMovesForAFigureForAPlayerNotBlocked :: String -> String -> Char -> [String]
listOfLawfillNonRotationMovesForAFigureForAPlayerNotBlocked board position player = [move | move <- lawfullMoves,not (targetPositionHasWhitesOrBlacksOnIt board move player), not(moveIsBlockedByFiguresInBetween board move)]
 where lawfullMoves = listOfLawfullNonRotationMovesForAFigureForAPlayer board position player 


--TESTED:SUCCESS
listOfMovesForAPlayer :: String -> Char -> [String]
listOfMovesForAPlayer board player = rotMove ++ goodMove
 where allPositions = [[x] ++ show y | x <- ['a'..'i'],y <- [1..9]]-- ['x'] == "x"
       rotMove = concat [listOfRotationMovesForAFigureForAPlayer board position player | position <- allPositions]
       goodMove = concat [listOfLawfullNonRotationMovesForAFigureForAPlayer board position player | position <- allPositions]

--recieves an 8 bit unsigned int and returns a list of its rotations
--TESTED:SUCCESS
listOfRotations :: Word8 -> [Integer]
listOfRotations x = [(toInteger (rotate x y))| y <- [0..7]]

--recieves a figure code and returns the list of the directions it faces
--TESTED:SUCCESS
figureFaces :: Integer -> [String]
xs = ["NN","NE","EE","SE","SS","SW","WW","NW"] 
figureFaces x = [xs !! y |y <- [0..7], testBit x y]

--recieves a board string and returns a list with all positions on the board
--TESTED:SUCCESS
boardStringToList :: String -> [String]
boardStringToList xs = splitOn "," [ if (not (x `elem` "/")) then x else ','| x <- xs]

--TESTED:SUCCESS
boardStringToListOfAPlayer :: String -> Char -> [String]
boardStringToListOfAPlayer xs player = [x | x <- boardList,if x == "" then True else (x !! 0) /= oppositePlayer]
 where boardList = boardStringToList xs
       oppositePlayer = if(player == 'w') then 'b' else 'w'



--recives a code of a figure and returns the type of a figure
--returns "-1" if there is no such figure
--TESTED:SUCCESS
whatFigure :: Integer -> String
whatFigure code 
 | length directionsFacing == 1 = "shield"
 | length directionsFacing == 2 = "probe"
 | length directionsFacing == 3 = "lance"
 | length directionsFacing == 4 = "commander"
 | otherwise = "-1"
 where directionsFacing = figureFaces code


--recives a code of a figure, moveDistance between two board points, direction, rotation
--returns Bool if the figure with this code can do this move


--TESTED:FAIL: test case 1: test with shield,moveDistance=1,boardDirection="d3-d4-0",rotation=False => ergibt False aber es ist True eigentlich
--TESTED:FAIL: test case 2: test with probe,moveDistance=2,boardDirection="c2-c4-0",rotation=False => ergibt False aber eigentlich True			
canMoveAccordingToRules :: Integer -> Integer -> String -> Bool -> Bool
canMoveAccordingToRules figureCode moveDistance boardDirection  rotation
 | moveDistance == 0 && rotation = True
 | canMoveInDirection && figureType == "shield" = moveDistance == 1
 | canMoveInDirection && figureType == "probe" && not rotation = moveDistance <= 2 && moveDistance > 0
 | canMoveInDirection && figureType == "lance" && not rotation = moveDistance <= 3 && moveDistance > 0
 | canMoveInDirection && figureType == "commander" && not rotation = moveDistance == 1
 | otherwise = False
 where canMoveInDirection = boardDirection `elem` figureFaces figureCode
       figureType = whatFigure figureCode

--recieves a position z.B "a9" and gives it position in the list
--TESTED:SUCCESS
getIndexOfAPosition :: String -> Integer
getIndexOfAPosition position = ((9 - row) * 9) + toInteger (ord column - ord 'a')
 where row = (read (drop 1 position)) :: Integer
       column = position !! 0 :: Char

--recieves a move string and an int 0 and gives beck the distance and direction of the move
--TESTED:SUCCESS
distanceAndDirectionOfTheMove :: String -> Integer -> (Integer,String)
distanceAndDirectionOfTheMove moveString j
 | indexOfStart == indexOfEnd = (0,"") --if a figure does not move
 | j == 9 = (-1,"")--target can not be reached in 1 move
 | mod (indexOfStart - j) 9 == 0 && mod (indexOfEnd - j) 9 == 0 && indexOfEnd - indexOfStart > 0 = (abs(((indexOfEnd - j) `div` 9) - ((indexOfStart - j) `div` 9)),"SS")
 | mod (indexOfStart - j) 9 == 0 && mod (indexOfEnd - j) 9 == 0 && indexOfEnd - indexOfStart < 0 = (abs(((indexOfEnd - j) `div` 9) - ((indexOfStart - j) `div` 9)),"NN")
 | indexOfStart >= i && indexOfStart <= (i + 9) && indexOfEnd >= i && indexOfEnd <= (i + 9) && indexOfEnd - indexOfStart > 0 = (abs(indexOfEnd - indexOfStart),"EE")
 | indexOfStart >= i && indexOfStart <= (i + 9) && indexOfEnd >= i && indexOfEnd <= (i + 9) && indexOfEnd - indexOfStart < 0 = (abs(indexOfEnd - indexOfStart),"WW")
 | biggerIndex == smallerIndex + 10 * j && indexOfEnd - indexOfStart > 0 = (j,"SE")
 | biggerIndex == smallerIndex + 10 * j && indexOfEnd - indexOfStart < 0 = (j,"NW")
 | biggerIndex == smallerIndex + 8 * j && indexOfEnd - indexOfStart > 0 = (j,"SW")
 | biggerIndex == smallerIndex + 8 * j && indexOfEnd - indexOfStart < 0 = (j,"NE")
 | otherwise = distanceAndDirectionOfTheMove moveString (j + 1)
   
 where i = j * 9
       indexOfStart = getIndexOfAPosition (take 2 moveString)
       indexOfEnd = getIndexOfAPosition (substring 3 5 moveString)
       biggerIndex = max indexOfStart indexOfEnd
       smallerIndex = min indexOfStart indexOfEnd

--TESTED:FAIL: test case 1 (target position has white) : ",w84,w41,w56,w170,w56,w41,w84,/,,w24,w40,w17,w40,w48,,/,,,,w16,w16,,,/,,,w16,,,,,/,,,b1,,,,,/,,,,,,,,/,,,,b1,b1,,,/,,b3,b130,b17,b130,b129,,/,b69,b146,b131,b170,b131,b146,b69," "d5-d6-0" 'b' has to return True but return False instead
--TESTED:FAIL: test case 2 (target position has black) : ",w84,w41,w56,w170,w56,w41,w84,/,,w24,w40,w17,w40,w48,,/,,,,w16,w16,,,/,,,w16,,,,,/,,,b1,,,,,/,,,,,,,,/,,,,b1,b1,,,/,,b3,b130,b17,b130,b129,,/,b69,b146,b131,b170,b131,b146,b69," "d6-d5-0" 'w' has to return True but return False instead
targetPositionHasWhitesOrBlacksOnIt :: String -> String -> Char -> Bool
targetPositionHasWhitesOrBlacksOnIt board moveString player
 | allPositions !! targetIndex == "" = False --check for the head "" exception
 | head (allPositions !! targetIndex) == player = True
 | otherwise =  False
 where allPositions = boardStringToList board 
       targetIndex = fromIntegral (getIndexOfAPosition (substring 3 5 moveString))

--TESTED:SUCCESS
moveIsBlockedByFiguresInBetween :: String -> String -> Bool
moveIsBlockedByFiguresInBetween board moveString 
 | allPositions !! (fromIntegral indexOfStart) == "" = True
 | moveDistance == 0 = False --if the figure does not move
 | moveDistance == 1 = False --if the move Distance is 1
 | moveDistance == -1 = True --if the target position can not be reached
 | (moveDirection == "WW" || moveDirection == "EE") && moveInBetweenPositionsFull board smallerIndex biggerIndex stepDistance count = True
 | (moveDirection == "SS" || moveDirection == "NN") && moveInBetweenPositionsFull board smallerIndex biggerIndex stepDistance count = True
 | (moveDirection == "SE" || moveDirection == "NW") && moveInBetweenPositionsFull board smallerIndex biggerIndex stepDistance count = True
 | (moveDirection == "SW" || moveDirection == "NE") && moveInBetweenPositionsFull board smallerIndex biggerIndex stepDistance count = True
 | otherwise = False 
 where moveDistance = (fst (distanceAndDirectionOfTheMove moveString 0))
       allPositions = boardStringToList board 
       moveDirection = (snd (distanceAndDirectionOfTheMove moveString 0))
       indexOfStart = getIndexOfAPosition (take 2 moveString)
       indexOfEnd = getIndexOfAPosition (substring 3 5 moveString)
       biggerIndex = max indexOfStart indexOfEnd
       smallerIndex = min indexOfStart indexOfEnd
       count = 0
       stepDistance = case moveDirection of "WW" -> 1  
                                            "EE" -> 1
                                            "SS" -> 9
                                            "NN" -> 9
                                            "SE" -> 10
                                            "NW" -> 10
                                            "SW" -> 8
                                            "NE" -> 8

--Hilfsfunktion for the moveIsBlockedByFiguresInBetween
--recursive for loop checking whether the inbetween positions are free
moveInBetweenPositionsFull :: String -> Integer -> Integer -> Integer -> Integer -> Bool
moveInBetweenPositionsFull board smallerIndex biggerIndex stepDistance count
 | index >= biggerIndex = False
 | allPositions !! fromIntegral(index) /= "" = True
 | otherwise = moveInBetweenPositionsFull board smallerIndex biggerIndex stepDistance (count + stepDistance)
 where allPositions = boardStringToList board 
       index = smallerIndex + stepDistance + count

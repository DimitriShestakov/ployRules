--- module (NICHT AENDERN!)
module PloyBot where

import Data.Char
import Util
import Data.Bits

--- external signatures (NICHT AENDERN!)
--getMove :: String -> String
listMoves :: String -> String

--- YOUR IMPLEMENTATION STARTS HERE ---
listMoves nowState = let list = map (split ',') (split '/' nowState) in --list from string separated by comma or / 
                    let bw = last nowState in
                    play bw list --Bot will play black or white
--z.B. map (split ',') (split '/' ",w84,w41,w56,w170,w56,w41,w84,/,,w24,w40,w17,w40,w48,,/,,,w16,w16,w16,,,/,,,,,,,,/,,,,,,,,/,,,,,,,,/,,,b1,b1,b1,,,/,,b3,b130,b17,b130,b129,,/,b69,b146,b131,b170,b131,b146,b69, b")
--Output:
--[["","w84","w41","w56","w170","w56","w41","w84"],
-- ["","","w24","w40","w17","w40","w48",""],
-- ["","","","w16","w16","w16","",""],
-- ["","","","","","","",""],
-- ["","","","","","","",""],
-- ["","","","","","","",""],
-- ["","","","b1","b1","b1","",""],
-- ["","","b3","b130","b17","b130","b129",""],
-- ["","b69","b146","b131","b170","b131","b146","b69"," b"]]

split :: Eq a => a -> [a] -> [[a]]--convert string to list(https://stackoverflow.com/questions/4978578/how-to-split-a-string-in-haskell)
split d [] = []
split d s = x : split d (drop 1 y) where (x,y) = span (/= d) s

--iterateElem :: Char -> [[String]] -> String
--iterateElem bw allElem = map 

play :: Char -> [[String]] -> String --play if x is white piece
play bw (x:xs):xt = if bw 'elem' x then
                    let zeiger = drop 1 x :: Integer in --get the number, convert to int
                        case popCount zeiger of --count the bit
                        1 -> move1 bw zeiger--shield
                        2 -> move2 bw zeiger--Probe
                        3 -> move3 bw zeiger--Lance
                        4 -> move4 bw zeiger--Commander
                 else x: play xs

move1 :: Char -> Integer -> String
move1 bw zeiger = 

--rotateL a b
--popCount a
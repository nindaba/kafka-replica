
#supported version
echo -n "000000230012000404b15f8f00096b61666b612d636c69000a6b61666b612d636c6904302e3100" | xxd -r -p | nc localhost 9092 | hexdump -C

#unsuported version
echo -n "000000230012674a4f74d28b00096b61666b612d636c69000a6b61666b612d636c6904302e3100" | xxd -r -p | nc localhost 9092 | hexdump -C

00 00 00 23 00 12 00 04 2b 14 42 c0 00 09 6b 61 66 6b 61 2d 63 6c 69 00 0a 6b 61 66 6b 61 2d 63 6c 69 04 30 2e 31 00

#request
00 00 00 23
00 12
00 04
04 b1 5f 8f

#response
00 00 00 88
04 b1 5f 8f
00 00
02
00 12
00 00
00 04


echo 0000002300127fd25e0dc7a900096b61 | xxd -r -p | nc localhost 9092 | hexdump -C
echo 00 00 00 23 00 12 00 04 3b 32 d0 45 00 09 6b 61 | tr -dd ' ' | xxd -r -p | nc localhost 9092 | hexdump -C
echo 000000131ec8a5b40000020012000300 | xxd -r -p | nc localhost 9092 | hexdump -C
echo 0000000804b15f8f00000000001304b15f8f000002001200030004000000000000| xxd -r -p | nc localhost 9092 | hexdump -C

00 00 00 88 4x8 message size
53 4d 9c 1f 4x8 correlation id
00 00 2x8       error code
02 1x8          size of the version array
00 12 2x8       api version
00 00 2x8       min version
00 04 2x8       max version
                throughtor
                tag_buffer

00 00 00 08     message size
53 4d 9c 1f     corr id
00 00           error code
00              size of array
00 00           api version
13 53           min
4d
9c 1f 00 00     idk
02              array size
00 12           version 18
00 03           min
00 04           max

00 00 00 08
04 b1 5f 8f
00 00
00
00 00
13 04
b1

00 00 00 08 04 b1 5f 8f  00 00 00 00 00 13 04 b1

00 00 00 08
04 b1 5f 8f


00 00 00 00
00 13
04 b1 5f 8f
00 00
02
00 12
00 03
00 04
00



00 00 00 08 04 b1 5f 8f  00 00 00 00 00 13 04 b1 5f 8f 00 00 02 00 12 00  03 00 04 00 00 00 00 00 00


echo 00 00 00 23 00 12 00 04 3b 32 d0 45 00 09 6b 61 | tr -dd ' ' | xxd -r -p | nc localhost 9092 | hexdump -C

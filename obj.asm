.model small
.stack 100h


.data
temp0 DB 'Hola mundo$'


.code
main PROC
MOV AX, @data
MOV DS, AX

MOV DX, OFFSET temp0
MOV AH, 09h
INT 21h
MOV AH, 4Ch
INT 21h
main ENDP
END main
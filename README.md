# SMAFilas
SMA T1~

GRUPO 9 - Eduardo Dornelles e João Almeida

# INSTRUÇÕES DE USO
# para manipular os dados de entrada é necessário editar o arquivo parametros.txt nessa ordem:
# EXEMPLO:

6,7,8,9 // seeds utilizada para geração dos numeros aleatórios.
//DADOS DA PRIMEIRA FILA (exemplo Fila 1 - G/G/2/3, chegadas entre 2..3, atendimento entre 2..5)
2 // intervalo inicial de tempo  para a chegada de clientes na fila;
3 // intervalo final de tempo  para a chegada de clientes na fila;
2 // intervalo inicial tempo de atendimento de um cliente na fila;
5 // intervalo final tempo de atendimento de um cliente na fila;
2 // número de servidores;
3 // capacidade da fila. -1 = capacidade infinita
F // Indica fim dos dados dessa fila (necessário!)

Para filas em tandem indique os dados logo abaixo do 'F' da fila anterior
(somente a primeira fila recebe intervalo de tempo para chegada de clientes)
Exemplo:

3 // intervalo inicial tempo de atendimento de um cliente na fila;
5 // intervalo final tempo de atendimento de um cliente na fila;
1 // número de servidores;
3 // capacidade da fila. -1 = capacidade infinita
F // Indica fim dos dados dessa fila (necessário!)

EXECUÇÃO:
no terminal dentro da pasta do projeto digite:
java -jar simulador.jar 5 
5 = quantidade de execuções.

OBSERVAÇÕES E VIDEO:
estão descritos no arquivo updates.txt
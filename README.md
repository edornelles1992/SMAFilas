# SMAFilas
SMA T1~

GRUPO 9 - Eduardo Dornelles e João Almeida

# INSTRUÇÕES DE USO
# para manipular os dados de entrada é necessário editar o arquivo parametros.txt nessa ordem:
# EXEMPLO:

SEEDS:6,7,8 // utilizada para geração dos numeros aleatórios.(cada seed corresponde a uma simulação. 4 seeds vão gerar 4 simulações)
<br />PRIMEIRO_CLIENTE_TEMPO:3 //tempo no qual vai chegar o primero cliente na fila
<br />2 // intervalo inicial de tempo  para a chegada de clientes na fila;
<br />3 // intervalo final de tempo  para a chegada de clientes na fila;
<br />2 // intervalo inicial tempo de atendimento de um cliente na fila;
<br />5 // intervalo final tempo de atendimento de um cliente na fila;
<br />2 // número de servidores;
<br />3 // capacidade da fila. -1 = capacidade infinita
<br />FIM_FILA // Indica fim dos dados dessa fila (necessário!)

EXECUÇÃO:
no terminal dentro da pasta do projeto digite:
java -jar simulador.jar
OBS: A quantidade de de execuções do simulador esta atrelada a quantidade de SEEDS informada nos parametros


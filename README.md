AprendizadoMaquina

Exercícos e trabalhos da disciplina de Aprendizado de Máquina do PPGC da UFPel - 2016/02

O projeto foi desenvolvido com o Netbeans 8.1 e JDK 1.8.0, com Maven, de forma a ser operacional em qualquer ferramenta de desenvolvimento.

Rodar na pasta ID3:

java -Xms6144m -Xmx6144m -jar target/ID3-1.0.jar [path/file.csv]

O sistema esta preparado para interpretar arquivos no formato .csv, onde a primeira linha e coluna são consideradas inuteis.
A última coluna esta sendo interpretada como a coluna do rótulo.

Modo visual usando JavaFx. Rodar na pasta Viewer:

java -Xms6144m -Xmx6144m -jar target/Viewer-1.0.jar 



A execução do treinamento de perceptrons e sua avaliação ainda é necessário mexer diretamente no código, nas classes do projeto Avaliacao


java -jar target/Avaliacao-1.0.jar mlp trainer fileTrainer.config


Arquivo de configuração da MLP

# configuração para treinamento de rede no linux

# detalhes dos arquivos
filetrainer=/home/mertins/Documentos/UFPel/Dr/AM/Trabalhos/mnist/mnist_train.csv
filetest=/home/mertins/Documentos/UFPel/Dr/AM/Trabalhos/mnist/mnist_test.csv

#filetrainer=/Users/mertins/Documents/UFPel/Dr/AprendizadoMaquina/mnist/mnist_train.csv
#filetest=/Users/mertins/Documents/UFPel/Dr/AprendizadoMaquina/mnist/mnist_test.csv

columnlabel=0
firstlineattribute=false

# configuração da MLP
hiddenlayer=(5,LOGISTIC)
outputlayer=10,LOGISTIC

# configurações do treinamento da MLP
blockifbaderr=false     # encerrar backtraining se erro piorar a época
normalize=true         # normalizar dados em 0 e 1 ?
ratetraining=0.01
moment=0.6
epoch=5

# configurações da avaliação da MLP
folderMLPs=/home/mertins/IARedeNeural/20170210_155447
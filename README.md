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
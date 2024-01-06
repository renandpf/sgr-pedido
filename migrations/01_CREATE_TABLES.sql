CREATE TABLE `Pedido` (
  `id` int NOT NULL AUTO_INCREMENT,
  `statusId` int NOT NULL,
  `dataCadastro` datetime NOT NULL,
  `dataConclusao` datetime DEFAULT NULL,
  `observacao` varchar(255) DEFAULT NULL,
  `clienteId` int DEFAULT NULL,
  PRIMARY KEY (`id`)
);

CREATE TABLE `Item` (
  `id` int NOT NULL AUTO_INCREMENT,
  `quantidade` int NOT NULL,
  `valorUnitario` float NOT NULL,
  `pedidoId` int NOT NULL,
  `produtoId` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_caf0721618f8177f10ca43a79f4` (`pedidoId`),
  CONSTRAINT `FK_caf0721618f8177f10ca43a79f4` FOREIGN KEY (`pedidoId`) REFERENCES `Pedido` (`id`)
);

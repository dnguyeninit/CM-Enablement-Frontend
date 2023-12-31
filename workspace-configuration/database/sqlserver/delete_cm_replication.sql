USE [cm_replication]
GO
DROP SCHEMA [cm_replication]
GO

USE [cm_replication]
GO
DROP USER [cm_replication]
GO

USE [master]
GO
DROP LOGIN [cm_replication]
GO

EXEC msdb.dbo.sp_delete_database_backuphistory @database_name = N'cm_replication'
GO
USE [master]
GO
ALTER DATABASE [cm_replication] SET SINGLE_USER WITH ROLLBACK IMMEDIATE
GO
USE [master]
GO
DROP DATABASE [cm_replication]
GO

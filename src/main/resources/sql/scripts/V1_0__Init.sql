USE [master]
GO
/****** Object:  Database [agilitydb]    Script Date: 10/02/2021 22:33:49 ******/
SET IMPLICIT_TRANSACTIONS OFF;
IF NOT EXISTS (SELECT * FROM sys.databases WHERE NAME = 'agilitydb')
BEGIN
CREATE DATABASE [agilitydb]
GO
USE [agilitydb]
GO
/****** Object:  Sequence [dbo].[hibernate_sequence]    Script Date: 10/02/2021 22:33:49 ******/
CREATE SEQUENCE [dbo].[hibernate_sequence]
 AS [bigint]
 START WITH 1
 INCREMENT BY 1
 MINVALUE -9223372036854775808
 MAXVALUE 9223372036854775807
 CACHE
GO
/****** Object:  Table [dbo].[Project]    Script Date: 10/02/2021 22:33:49 ******/
CREATE TABLE [dbo].[Project](
	[id] [bigint] NOT NULL,
	[title] [varchar](255) NULL,
PRIMARY KEY CLUSTERED
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[ProjectsUsers]    Script Date: 10/02/2021 22:33:49 ******/
CREATE TABLE [dbo].[ProjectsUsers](
	[project_id] [bigint] NOT NULL,
	[user_id] [bigint] NOT NULL,
PRIMARY KEY CLUSTERED
(
	[project_id] ASC,
	[user_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[Task]    Script Date: 10/02/2021 22:33:49 ******/
CREATE TABLE [dbo].[Task](
	[id] [bigint] NOT NULL,
	[description] [varchar](255) NULL,
	[priority] [varchar](255) NULL,
	[status] [varchar](255) NULL,
	[title] [varchar](255) NULL,
	[assignee_id] [bigint] NULL,
	[project_id] [bigint] NULL,
PRIMARY KEY CLUSTERED
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[UserRole]    Script Date: 10/02/2021 22:33:49 ******/
CREATE TABLE [dbo].[UserRole](
	[id] [bigint] NOT NULL,
	[name] [varchar](255) NULL,
PRIMARY KEY CLUSTERED
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[Users]    Script Date: 10/02/2021 22:33:49 ******/
CREATE TABLE [dbo].[Users](
	[id] [bigint] NOT NULL,
	[email] [varchar](255) NULL,
	[firstName] [varchar](255) NULL,
	[lastName] [varchar](255) NULL,
	[password] [varchar](255) NULL,
	[username] [varchar](255) NULL,
	[role_id] [bigint] NULL,
PRIMARY KEY CLUSTERED
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
INSERT [dbo].[Project] ([id], [title]) VALUES (8, N'Test project')
GO
INSERT [dbo].[ProjectsUsers] ([project_id], [user_id]) VALUES (8, 6)
GO
INSERT [dbo].[Task] ([id], [description], [priority], [status], [title], [assignee_id], [project_id]) VALUES (9, N'Hitno hitno!', N'High', N'Testing', N'[Hitno!] Novi task', 7, 8)
GO
INSERT [dbo].[UserRole] ([id], [name]) VALUES (1, N'ROLE_ADMIN')
INSERT [dbo].[UserRole] ([id], [name]) VALUES (2, N'ROLE_USER')
INSERT [dbo].[UserRole] ([id], [name]) VALUES (3, N'ROLE_PROJECT_MANAGER')
GO
INSERT [dbo].[Users] ([id], [email], [firstName], [lastName], [password], [username], [role_id]) VALUES (4, N'admin@agility.com', N'Admin', N'Admin', N'admin', N'admin', 1)
INSERT [dbo].[Users] ([id], [email], [firstName], [lastName], [password], [username], [role_id]) VALUES (5, N'pm@agility.com', N'Project', N'Manager', N'pm', N'pm', 3)
INSERT [dbo].[Users] ([id], [email], [firstName], [lastName], [password], [username], [role_id]) VALUES (6, N'test@test.com', N'Test', N'User', N'test', N'testuser', 2)
INSERT [dbo].[Users] ([id], [email], [firstName], [lastName], [password], [username], [role_id]) VALUES (7, N'bobanzsavic@gmail.com', N'Boban', N'Savic', N'test', N'bsavic', 2)
/****** Object:  Index [UK_9ytiqf3wdk0iv4jcttcngwr9a]    Script Date: 10/02/2021 22:33:49 ******/
ALTER TABLE [dbo].[Project] ADD  CONSTRAINT [UK_9ytiqf3wdk0iv4jcttcngwr9a] UNIQUE NONCLUSTERED
(
	[title] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
/****** Object:  Index [UK_ckvnvh6xmm7ihmid6bfx1jg3b]    Script Date: 10/02/2021 22:33:49 ******/
ALTER TABLE [dbo].[Task] ADD  CONSTRAINT [UK_ckvnvh6xmm7ihmid6bfx1jg3b] UNIQUE NONCLUSTERED
(
	[title] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
GO
SET ANSI_PADDING ON
GO
/****** Object:  Index [UK_23y4gd49ajvbqgl3psjsvhff6]    Script Date: 10/02/2021 22:33:49 ******/
ALTER TABLE [dbo].[Users] ADD  CONSTRAINT [UK_23y4gd49ajvbqgl3psjsvhff6] UNIQUE NONCLUSTERED
(
	[username] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
GO
SET ANSI_PADDING ON
GO
/****** Object:  Index [UK_ncoa9bfasrql0x4nhmh1plxxy]    Script Date: 10/02/2021 22:33:49 ******/
ALTER TABLE [dbo].[Users] ADD  CONSTRAINT [UK_ncoa9bfasrql0x4nhmh1plxxy] UNIQUE NONCLUSTERED
(
	[email] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
GO
ALTER TABLE [dbo].[ProjectsUsers]  WITH CHECK ADD  CONSTRAINT [FK17rp8yxv8raud3bcabap7r2ei] FOREIGN KEY([project_id])
REFERENCES [dbo].[Project] ([id])
GO
ALTER TABLE [dbo].[ProjectsUsers] CHECK CONSTRAINT [FK17rp8yxv8raud3bcabap7r2ei]
GO
ALTER TABLE [dbo].[ProjectsUsers]  WITH CHECK ADD  CONSTRAINT [FKcln7da8bvb59555x6ghjgkdkh] FOREIGN KEY([user_id])
REFERENCES [dbo].[Users] ([id])
GO
ALTER TABLE [dbo].[ProjectsUsers] CHECK CONSTRAINT [FKcln7da8bvb59555x6ghjgkdkh]
GO
ALTER TABLE [dbo].[Task]  WITH CHECK ADD  CONSTRAINT [FKkkcat6aybe3nbvhc54unstxm6] FOREIGN KEY([project_id])
REFERENCES [dbo].[Project] ([id])
GO
ALTER TABLE [dbo].[Task] CHECK CONSTRAINT [FKkkcat6aybe3nbvhc54unstxm6]
GO
ALTER TABLE [dbo].[Task]  WITH CHECK ADD  CONSTRAINT [FKm58ppaj5k88gvxwcnn9n8dbtt] FOREIGN KEY([assignee_id])
REFERENCES [dbo].[Users] ([id])
GO
ALTER TABLE [dbo].[Task] CHECK CONSTRAINT [FKm58ppaj5k88gvxwcnn9n8dbtt]
GO
ALTER TABLE [dbo].[Users]  WITH CHECK ADD  CONSTRAINT [FK1erb0m3aye7jwmc1a6l42dr7p] FOREIGN KEY([role_id])
REFERENCES [dbo].[UserRole] ([id])
GO
ALTER TABLE [dbo].[Users] CHECK CONSTRAINT [FK1erb0m3aye7jwmc1a6l42dr7p]
GO
END


INSERT INTO pathmind.User (id, name, email, password) VALUES (1, 'Steph', 'steph@followsteph.com', 'password');
INSERT INTO pathmind.User (id, name, email, password) VALUES (2, 'Bob', 'bob@io.skymind.io', 'password');

INSERT INTO pathmind.Project (id, user_id, name, date_created) VALUES (1, 1, 'Coffee Shop', '2019-08-23');
INSERT INTO pathmind.Project (id, user_id, name, date_created) VALUES (2, 1, 'Traffic Simulation', '2019-08-24');

INSERT INTO pathmind.Experiment (id, project_id, name, date, run_type, score) VALUES (1, 1, 'Coffee Shop Experiment 1', '2019-08-01', 1, 10);
INSERT INTO pathmind.Experiment (id, project_id, name, date, run_type, score) VALUES (2, 1, 'Coffee Shop Experiment 2', '2019-08-02', 1, 20);
INSERT INTO pathmind.Experiment (id, project_id, name, date, run_type, score) VALUES (3, 1, 'Coffee Shop Experiment 3', '2019-08-03', 1, 30);
INSERT INTO pathmind.Experiment (id, project_id, name, date, run_type, score) VALUES (4, 2, 'Traffic Simulation Experiment 1', '2019-08-04', 1, 40);

INSERT INTO pathmind.Run (id, experiment_id, name, date) VALUES (1, 1, 'Coffee Shop Run 1', '2019-08-01');
INSERT INTO pathmind.Run (id, experiment_id, name, date) VALUES (2, 1, 'Coffee Shop Run 2', '2019-08-02');
INSERT INTO pathmind.Run (id, experiment_id, name, date) VALUES (3, 1, 'Coffee Shop Run 3', '2019-08-03');
INSERT INTO pathmind.Run (id, experiment_id, name, date) VALUES (4, 2, 'Coffee Shop Run 1', '2019-08-04');


INSERT INTO User (id, name, email, password) VALUES (1, 'Steph', 'steph@followsteph.com', 'password');

INSERT INTO Project (id, name, date_created) VALUES (1, 'Coffee Shop', '2019-08-23');
INSERT INTO Project (id, name, date_created) VALUES (2, 'Traffic Simulation', '2019-08-24');

INSERT INTO Experiment (id, project_id, name, date, run_type, score) VALUES (1, 1, 'Coffee Shop Experiment 1', '2019-08-01', 1, 10);
INSERT INTO Experiment (id, project_id, name, date, run_type, score) VALUES (2, 1, 'Coffee Shop Experiment 2', '2019-08-02', 1, 20);
INSERT INTO Experiment (id, project_id, name, date, run_type, score) VALUES (3, 1, 'Coffee Shop Experiment 3', '2019-08-03', 1, 30);
INSERT INTO Experiment (id, project_id, name, date, run_type, score) VALUES (4, 2, 'Traffic Simulation Experiment 1', '2019-08-04', 1, 40);

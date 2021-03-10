"""
 LoggerInit.py:
 Description: Class for setting up the Logger instance
 Created by : Daniel Jaramillo
"""
import logging
import sys
from logging.handlers import TimedRotatingFileHandler

class LoggerInit:

    def __init__(self):
        # self.log_file=log_file
        # self.interval=interval
        #Log file entry format
        self.formatter=logging.Formatter("%(asctime)s - %(name)s - " +
                                        "%(funcName)s " +
                                        "%(levelname)s - %(message)s")
        #Create the console handler
        self.console_handler=self.get_console_handler()
        #Create the file handler
        # self.file_handler=self.get_file_handler()

    #Instance to log to the screen
    def get_console_handler(self):
        console_handler=logging.StreamHandler(sys.stdout)
        console_handler.setFormatter(self.formatter)
        return console_handler

    #Instance to log to the log file rotated every interval
    # def get_file_handler(self):
    #     file_handler=TimedRotatingFileHandler(self.log_file,when='midnight',
    #                                           interval=self.interval)
    #     file_handler.setFormatter(self.formatter)
    #     return file_handler

    #Create and return the logger
    def get_logger(self,logger_name):
        logger=logging.getLogger(logger_name)
        logger.setLevel(logging.DEBUG)
        logger.addHandler(self.console_handler)
        # logger.addHandler(self.file_handler)
        logger.propagate=False
        return logger


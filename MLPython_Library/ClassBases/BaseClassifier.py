import numpy as np

class ClassifierBase:
    def __init__(self,solver,batch_size,learning_rate,beta,C,regularization,validation,n_iter):
        self._solver = solver
        self._batch_size = batch_size
        self._alpha = learning_rate
        self._beta = beta
        self._C = C
        self._r_type = regularization
        self._val = validation
        self._max_iter = n_iter
        self._params = {}
        self._loss = 0.0
    
    def activation(self,X,func):
        if(func == 'identity'):
            return X
        elif(func == 'tanh'):
            return np.tanh(X)
        elif(func == 'relu'):
            return np.maximum(X,0)
        elif(func == 'softmax'):
            val = np.sum(np.exp(X),axis = 0,keepdims = True)

            val = np.exp(X)/val
            return val
    
    def d_activation(self,X,func):
        if(func == 'identity'):
            pass
        elif(func == 'tanh'):
            return (1-X**2)
        elif func == 'relu':
            return (X>0)